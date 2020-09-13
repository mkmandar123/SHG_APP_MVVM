package com.digitaldetox.aww.ui.main.root

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.ORDER_BY_DESC_DATE_UPDATED_SUBUSER
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBUSER_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBUSER_FILTER_USERNAME
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBUSER_ORDER_ASC
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBUSER_ORDER_DESC
import com.digitaldetox.aww.ui.main.root.state.ROOT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.viewmodel.*
import com.digitaldetox.aww.util.Constants
import com.digitaldetox.aww.util.Constants.Companion.SUBREDDIT_MEMBERS_KEY
import com.digitaldetox.aww.util.Constants.Companion.SUBREDDIT_NAME_KEY
import com.digitaldetox.aww.util.ErrorHandling.Companion.isPaginationDone
import com.digitaldetox.aww.util.StateMessageCallback
import com.digitaldetox.aww.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_subuser.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import loadFirstPageSubuser
import nextPageSubuser
import refreshFromCacheSubuser
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SubuserFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions
) : BaseRootFragment(R.layout.fragment_subuser, viewModelFactory),
    SubuserListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: SubuserListAdapter
    private var requestManager: RequestManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        savedInstanceState?.let { inState ->
            Log.d(TAG, "RootViewState: inState is NOT null")
            (inState[ROOT_VIEW_STATE_BUNDLE_KEY] as RootViewState?)?.let { viewState ->
                Log.d(TAG, "RootViewState: restoring view state: ${viewState}")
                viewModel.setViewState(viewState)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value


        viewState?.subuserFields?.subuserList = ArrayList()

        outState.putParcelable(
            ROOT_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subredditName =
            arguments?.getString(SUBREDDIT_NAME_KEY)!!


        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "SHG Members in ${subredditName.toString()}"
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)
        setupGlide()

        initRecyclerView()
        subscribeObservers()
        uiCommunicationListener.expandAppBar()

    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCacheSubuser()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        subuser_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerStateSubuser(lmState)
        }
    }

    private fun subscribeObservers() {


        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            recyclerAdapter.notifyDataSetChanged()
            if (viewState != null) {

                val subredditData =
                    arguments?.getStringArrayList(SUBREDDIT_MEMBERS_KEY)!!

                val filterAndOrderSubuser =
                    viewModel.getOrderSubuser() + viewModel.getFilterSubuser()





                recyclerAdapter.apply {
                    viewState.subuserFields.subuserList?.let {
                        preloadGlideImages(
                            requestManager = requestManager as RequestManager,
                            list = it
                        )
                    }


                    when {

                        filterAndOrderSubuser.contains(ORDER_BY_DESC_DATE_UPDATED_SUBUSER) -> {

                            submitList(
                                subuserList = subredditData.sorted()
                                ,
                                isQueryExhausted = viewState.subuserFields.isQueryExhausted ?: true
                            )

                        }
                        else -> {
                            submitList(
                                subuserList = subredditData.sortedDescending()
                                ,
                                isQueryExhausted = viewState.subuserFields.isQueryExhausted ?: true
                            )
                        }
                    }


                }

            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (isPaginationDone(stateMessage.response.message)) {
                    viewModel.setQueryExhaustedSubuser(true)
                    viewModel.clearStateMessage()
                } else {
                    uiCommunicationListener.onResponseReceived(
                        response = it.response,
                        stateMessageCallback = object : StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                }
            }
        })
    }

//    private fun initSearchView(menu: Menu) {
//        activity?.apply {
//            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
//            searchView = menu.findItem(R.id.action_search_subuser).actionView as SearchView
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            searchView.maxWidth = Integer.MAX_VALUE
//            searchView.setIconifiedByDefault(true)
//            searchView.isSubmitButtonEnabled = true
//        }
//
//
//        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
//        searchPlate.setOnEditorActionListener { v, actionId, event ->
//
//            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
//                || actionId == EditorInfo.IME_ACTION_SEARCH
//            ) {
//                val searchQuery = v.text.toString()
//                Log.e(TAG, "SearchView: (keyboard or arrow) executing search...: ${searchQuery}")
//                viewModel.setQuerySubuser(searchQuery).let {
//                    onSubuserSearchOrFilter()
//                }
//            }
//            true
//        }
//
//
//        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
//        searchButton.setOnClickListener {
//            val searchQuery = searchPlate.text.toString()
//            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")
//            viewModel.setQuerySubuser(searchQuery).let {
//                onSubuserSearchOrFilter()
//            }
//
//        }
//    }

    private fun onSubuserSearchOrFilter() {
        viewModel.loadFirstPageSubuser().let {
            resetUI()
        }
    }

    private fun resetUI() {
        subuser_recyclerview.smoothScrollToPosition(0)
        uiCommunicationListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun initRecyclerView() {

        subuser_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@SubuserFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = SubuserListAdapter(
                requestManager as RequestManager,
                this@SubuserFragment
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "SubuserFragment: attempting to load next page...")
                        viewModel.nextPageSubuser()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    private fun setupGlide() {
        val requestOptions = RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)

        activity?.let {
            requestManager = Glide.with(it)
                .applyDefaultRequestOptions(requestOptions)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu_subuser, menu)
//        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_filter_settings_subuser -> {
                showFilterDialog()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onItemSelected(position: Int, item: String) {
//
//        var bundle = bundleOf(
//            Constants.USERLOANREQUEST_AUTHORSENDER_KEY to viewModel.getUserloanrequest().authorsender
//        )
//        viewModel.setSubuserPassAuthorsenderName(item)
//
//        findNavController().navigate(R.id.action_subuserFragment_to_userloanrequestFragment, bundle)
    }

    override fun onItemLongSelected(position: Int, item: String) {
//        var bundle = bundleOf(
//            Constants.USERSAVINGREQUEST_AUTHORSENDER_KEY to viewModel.getUsersavingrequest().authorsender
//        )
//        viewModel.setSubuserPassAuthorsenderName(item)
//
//        findNavController().navigate(R.id.action_subuserFragment_to_usersavingrequestFragment, bundle)

    }

    override fun onLoanButtonSelected(position: Int, item: String) {

        var bundle = bundleOf(
            Constants.USERLOANREQUEST_AUTHORSENDER_KEY to viewModel.getUserloanrequest().authorsender
        )
        viewModel.setSubuserPassAuthorsenderName(item)

        findNavController().navigate(R.id.action_subuserFragment_to_userloanrequestFragment, bundle)

    }

    override fun onSavingButtonSelected(position: Int, item: String) {
        var bundle = bundleOf(
            Constants.USERSAVINGREQUEST_AUTHORSENDER_KEY to viewModel.getUsersavingrequest().authorsender
        )
        viewModel.setSubuserPassAuthorsenderName(item)

        findNavController().navigate(R.id.action_subuserFragment_to_usersavingrequestFragment, bundle)

     }

    override fun restoreListPosition() {
        viewModel.viewState.value?.subuserFields?.layoutManagerState?.let { lmState ->
            subuser_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        subuser_recyclerview.adapter = null
        requestManager = null
    }

    override fun onRefresh() {
        onSubuserSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    fun showFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_subuser_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilterSubuser()
            val order = viewModel.getOrderSubuser()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    SUBUSER_FILTER_DATE_UPDATED -> check(R.id.filter_date)
                    SUBUSER_FILTER_USERNAME -> check(R.id.filter_author)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    SUBUSER_ORDER_ASC -> check(R.id.filter_asc)
                    SUBUSER_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: apply filter.")

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_author -> SUBUSER_FILTER_USERNAME
                        R.id.filter_date -> SUBUSER_FILTER_DATE_UPDATED
                        else -> SUBUSER_FILTER_DATE_UPDATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterOptionsSubuser(newFilter, newOrder)
                    setSubuserFilterSubuser(newFilter)
                    setSubuserOrderSubuser(newOrder)
                }

                onSubuserSearchOrFilter()

                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: cancelling filter.")
                dialog.dismiss()
            }

            dialog.show()
        }
    }


}








