package com.digitaldetox.aww.ui.main.root

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERLOANREQUEST_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERLOANREQUEST_FILTER_USERNAME
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERLOANREQUEST_ORDER_ASC
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERLOANREQUEST_ORDER_DESC
import com.digitaldetox.aww.ui.main.root.state.ROOT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.viewmodel.*
import com.digitaldetox.aww.util.Constants.Companion.SUBREDDIT_MEMBERS_KEY
import com.digitaldetox.aww.util.Constants.Companion.USERLOANREQUEST_AUTHORSENDER_KEY
import com.digitaldetox.aww.util.ErrorHandling.Companion.isPaginationDone
import com.digitaldetox.aww.util.StateMessageCallback
import com.digitaldetox.aww.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_userloanrequest.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import loadFirstPageUserloanrequest
import nextPageUserloanrequest
import refreshFromCacheUserloanrequest
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class UserloanrequestFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions
) : BaseRootFragment(R.layout.fragment_userloanrequest, viewModelFactory),
    UserloanrequestListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {
    private var albumId: String? = null
    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: UserloanrequestListAdapter
    private var requestManager: RequestManager? = null
    var clickedButtonFragm = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        savedInstanceState?.let { inState ->
            Log.d(TAG, "RootViewState: inState is NOT null")
            (inState[ROOT_VIEW_STATE_BUNDLE_KEY] as RootViewState?)?.let { viewState ->
                Log.d(TAG, "RootViewState: restoring view state: ${viewState}")
                viewModel.setViewState(viewState)
            }
        }

        albumId = "userloanrequest_create_album_id"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value


        viewState?.userloanrequestFields?.userloanrequestList = ArrayList()

        outState.putParcelable(
            ROOT_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.shg_loan_requests)
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)
        setupGlide()
        initRecyclerView()
        subscribeObservers()
        uiCommunicationListener.expandAppBar()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCacheUserloanrequest()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        userloanrequest_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerStateUserloanrequest(lmState)
        }
    }

    private fun subscribeObservers() {


        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            recyclerAdapter.notifyDataSetChanged()
            if (viewState != null) {
                recyclerAdapter.apply {
                    viewState.userloanrequestFields.userloanrequestList?.let {
                        preloadGlideImages(
                            requestManager = requestManager as RequestManager,
                            list = it
                        )
                    }

                    Log.d(TAG, "subscribeObservers: 142:viewState.userloanrequestFields.userloanrequestList ${viewState.userloanrequestFields.userloanrequestList} ")

                    submitList(
                        userloanrequestList = viewState.userloanrequestFields.userloanrequestList,
                        isQueryExhausted = viewState.userloanrequestFields.isQueryExhausted ?: true
                    )
                }

            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (isPaginationDone(stateMessage.response.message)) {
                    viewModel.setQueryExhaustedUserloanrequest(true)
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

        viewModel.loadRoot(albumId)
    }

    private fun initSearchView(menu: Menu) {
        activity?.apply {
            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search_userloanrequest).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.maxWidth = Integer.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }


        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                val searchQuery = v.text.toString()
                Log.e(TAG, "SearchView: (keyboard or arrow) executing search...: ${searchQuery}")
                viewModel.setQueryUserloanrequest(searchQuery).let {
                    onUserloanrequestSearchOrFilter()
                }
            }
            true
        }


        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")
            viewModel.setQueryUserloanrequest(searchQuery).let {
                onUserloanrequestSearchOrFilter()
            }

        }
    }

    private fun onUserloanrequestSearchOrFilter() {
        viewModel.loadFirstPageUserloanrequest().let {
            resetUI()
        }
    }

    private fun resetUI() {
        userloanrequest_recyclerview.smoothScrollToPosition(0)
        uiCommunicationListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun initRecyclerView() {

        userloanrequest_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@UserloanrequestFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = UserloanrequestListAdapter(
                requestManager as RequestManager,
                this@UserloanrequestFragment
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "UserloanrequestFragment: attempting to load next page...")
                        viewModel.nextPageUserloanrequest()
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
        inflater.inflate(R.menu.search_menu_userloanrequest, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_filter_settings_userloanrequest -> {
                showFilterDialog()
                return true
            }

            R.id.action_create_userloanrequest -> {
                var bundle = bundleOf(
                    USERLOANREQUEST_AUTHORSENDER_KEY to viewModel.getAuthorsenderNamePassSubuser(),
                    SUBREDDIT_MEMBERS_KEY to viewModel.getSubreddit().title
                )
                findNavController().navigate(
                    R.id.action_userloanrequestFragment_to_createUserloanrequestFragment,
                    bundle
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }




    override fun onItemLongSelected(position: Int, item: UserloanrequestRoom) {

//        viewModel.setUserloanrequest(item)
//        try {
//
//            viewModel.setUpdatedTitleUserloanrequest(item.title)
//            viewModel.setUpdatedBodyUserloanrequest(item.body)
//            viewModel.setUpdatedLoanamountUserloanrequest(item.loanamount.toString().toInt())
//            findNavController().navigate(R.id.action_userloanrequestFragment_to_updateUserloanrequestFragment)
//        } catch (e: Exception) {
//
//            Log.d(TAG, "Exception: ${e.message}")
//        }

    }

    override fun onUpdateLoanButtonSelected(position: Int, item: UserloanrequestRoom) {

        viewModel.setUserloanrequest(item)
        try {

            viewModel.setUpdatedTitleUserloanrequest(item.title)
            viewModel.setUpdatedBodyUserloanrequest(item.body)
            viewModel.setUpdatedLoanamountUserloanrequest(item.loanamount.toString().toInt())
            findNavController().navigate(R.id.action_userloanrequestFragment_to_updateUserloanrequestFragment)
        } catch (e: Exception) {

            Log.d(TAG, "Exception: ${e.message}")
        }



    }

    override fun onItemSelected(position: Int, item: UserloanrequestRoom) {
        viewModel.setUserloanrequest(item)

        findNavController().navigate(R.id.action_userloanrequestFragment_to_viewUserloanrequestFragment)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.userloanrequestFields?.layoutManagerState?.let { lmState ->
            userloanrequest_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        userloanrequest_recyclerview.adapter = null
        requestManager = null
    }

    override fun onRefresh() {
        onUserloanrequestSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    fun showFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_userloanrequest_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilterUserloanrequest()
            val order = viewModel.getOrderUserloanrequest()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    USERLOANREQUEST_FILTER_DATE_UPDATED -> check(R.id.filter_date)
                    USERLOANREQUEST_FILTER_USERNAME -> check(R.id.filter_author)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    USERLOANREQUEST_ORDER_ASC -> check(R.id.filter_asc)
                    USERLOANREQUEST_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: apply filter.")

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_author -> USERLOANREQUEST_FILTER_USERNAME
                        R.id.filter_date -> USERLOANREQUEST_FILTER_DATE_UPDATED
                        else -> USERLOANREQUEST_FILTER_DATE_UPDATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterOptionsUserloanrequest(newFilter, newOrder)
                    setUserloanrequestFilterUserloanrequest(newFilter)
                    setUserloanrequestOrderUserloanrequest(newOrder)
                }

                onUserloanrequestSearchOrFilter()

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








