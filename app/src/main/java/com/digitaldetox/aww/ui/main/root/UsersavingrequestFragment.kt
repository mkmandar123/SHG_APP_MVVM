package com.digitaldetox.aww.ui.main.root

import android.app.SearchManager
import android.content.Context
import android.content.Context.SEARCH_SERVICE
import android.content.SharedPreferences
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
import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERSAVINGREQUEST_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERSAVINGREQUEST_FILTER_USERNAME
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERSAVINGREQUEST_ORDER_ASC
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.USERSAVINGREQUEST_ORDER_DESC
import com.digitaldetox.aww.ui.main.root.state.ROOT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.viewmodel.*
import com.digitaldetox.aww.util.Constants.Companion.SUBREDDIT_MEMBERS_KEY
import com.digitaldetox.aww.util.Constants.Companion.USERSAVINGREQUEST_AUTHORSENDER_KEY
import com.digitaldetox.aww.util.ErrorHandling.Companion.isPaginationDone
import com.digitaldetox.aww.util.StateMessageCallback
import com.digitaldetox.aww.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_usersavingrequest.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import loadFirstPageUsersavingrequest
import nextPageUsersavingrequest
import refreshFromCacheUsersavingrequest
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class UsersavingrequestFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions
) : BaseRootFragment(R.layout.fragment_usersavingrequest, viewModelFactory),
    UsersavingrequestListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {
    private var albumId: String? = null
    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: UsersavingrequestListAdapter
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

        albumId = "usersavingrequest_create_album_id"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value


        viewState?.usersavingrequestFields?.usersavingrequestList = ArrayList()

        outState.putParcelable(
            ROOT_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.shg_saving_requests)

        val mPrefs: SharedPreferences = activity!!.getSharedPreferences("accountkey", Context.MODE_PRIVATE)
        val accountSavingtargetText = mPrefs.getString("accountSavingtarget", "0")
        total_savings.setText(accountSavingtargetText.toString())


        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)
        setupGlide()
        initRecyclerView()
        subscribeObservers()
        uiCommunicationListener.expandAppBar()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCacheUsersavingrequest()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        usersavingrequest_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerStateUsersavingrequest(lmState)
        }
    }

    private fun subscribeObservers() {


        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            recyclerAdapter.notifyDataSetChanged()
            if (viewState != null) {
                recyclerAdapter.apply {
                    viewState.usersavingrequestFields.usersavingrequestList?.let {
                        var total = 0
                        for (element in it) {
                            total += element.savingamount
                            Log.d(TAG, "preloadGlideImages: totoal: ${total}")
                        }

//                        total_savings.setText("cool total savings: ${total}")

                        preloadGlideImages(
                            requestManager = requestManager as RequestManager,
                            list = it
                        )
                    }

                    Log.d(
                        TAG,
                        "subscribeObservers: 142:viewState.usersavingrequestFields.usersavingrequestList ${viewState.usersavingrequestFields.usersavingrequestList} "
                    )

                    submitList(
                        usersavingrequestList = viewState.usersavingrequestFields.usersavingrequestList,
                        isQueryExhausted = viewState.usersavingrequestFields.isQueryExhausted
                            ?: true
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
                    viewModel.setQueryExhaustedUsersavingrequest(true)
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
            searchView =
                menu.findItem(R.id.action_search_usersavingrequest).actionView as SearchView
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
                viewModel.setQueryUsersavingrequest(searchQuery).let {
                    onUsersavingrequestSearchOrFilter()
                }
            }
            true
        }


        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")
            viewModel.setQueryUsersavingrequest(searchQuery).let {
                onUsersavingrequestSearchOrFilter()
            }

        }
    }

    private fun onUsersavingrequestSearchOrFilter() {
        viewModel.loadFirstPageUsersavingrequest().let {
            resetUI()
        }
    }

    private fun resetUI() {
        usersavingrequest_recyclerview.smoothScrollToPosition(0)
        uiCommunicationListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun initRecyclerView() {

        usersavingrequest_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@UsersavingrequestFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = UsersavingrequestListAdapter(
                requestManager as RequestManager,
                this@UsersavingrequestFragment
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "UsersavingrequestFragment: attempting to load next page...")
                        viewModel.nextPageUsersavingrequest()
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
        inflater.inflate(R.menu.search_menu_usersavingrequest, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_filter_settings_usersavingrequest -> {
                showFilterDialog()
                return true
            }


            R.id.action_saving_goal_usersavingrequest -> {
                var bundle = bundleOf(
                    USERSAVINGREQUEST_AUTHORSENDER_KEY to viewModel.getAuthorsenderNamePassSubuser(),
                    SUBREDDIT_MEMBERS_KEY to viewModel.getSubreddit().title
                )
                findNavController().navigate(
                    R.id.action_usersavingrequestFragment_to_createUsersavingrequestFragment,
                    bundle
                )
                return true
            }


            R.id.action_create_usersavingrequest -> {
                var bundle = bundleOf(
                    USERSAVINGREQUEST_AUTHORSENDER_KEY to viewModel.getAuthorsenderNamePassSubuser(),
                    SUBREDDIT_MEMBERS_KEY to viewModel.getSubreddit().title
                )
                findNavController().navigate(
                    R.id.action_usersavingrequestFragment_to_createUsersavingrequestFragment,
                    bundle
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onItemLongSelected(position: Int, item: UsersavingrequestRoom) {
//        viewModel.setUsersavingrequest(item)
//        try {
//
//            viewModel.setUpdatedTitleUsersavingrequest(item.title)
//            viewModel.setUpdatedBodyUsersavingrequest(item.body)
//            viewModel.setUpdatedSavingamountUsersavingrequest(item.savingamount)
//            findNavController().navigate(R.id.action_usersavingrequestFragment_to_updateUsersavingrequestFragment)
//        } catch (e: Exception) {
//
//            Log.d(TAG, "Exception: ${e.message}")
//        }

    }

    override fun onUpdateSavingButtonSelected(position: Int, item: UsersavingrequestRoom) {

        viewModel.setUsersavingrequest(item)
        try {

            viewModel.setUpdatedTitleUsersavingrequest(item.title)
            viewModel.setUpdatedBodyUsersavingrequest(item.body)
            viewModel.setUpdatedSavingamountUsersavingrequest(item.savingamount)
            findNavController().navigate(R.id.action_usersavingrequestFragment_to_updateUsersavingrequestFragment)
        } catch (e: Exception) {

            Log.d(TAG, "Exception: ${e.message}")
        }




    }

    override fun onItemSelected(position: Int, item: UsersavingrequestRoom) {
        viewModel.setUsersavingrequest(item)

        findNavController().navigate(R.id.action_usersavingrequestFragment_to_viewUsersavingrequestFragment)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.usersavingrequestFields?.layoutManagerState?.let { lmState ->
            usersavingrequest_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        usersavingrequest_recyclerview.adapter = null
        requestManager = null
    }

    override fun onRefresh() {
        onUsersavingrequestSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    fun showFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_usersavingrequest_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilterUsersavingrequest()
            val order = viewModel.getOrderUsersavingrequest()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    USERSAVINGREQUEST_FILTER_DATE_UPDATED -> check(R.id.filter_date)
                    USERSAVINGREQUEST_FILTER_USERNAME -> check(R.id.filter_author)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    USERSAVINGREQUEST_ORDER_ASC -> check(R.id.filter_asc)
                    USERSAVINGREQUEST_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: apply filter.")

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_author -> USERSAVINGREQUEST_FILTER_USERNAME
                        R.id.filter_date -> USERSAVINGREQUEST_FILTER_DATE_UPDATED
                        else -> USERSAVINGREQUEST_FILTER_DATE_UPDATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterOptionsUsersavingrequest(newFilter, newOrder)
                    setUsersavingrequestFilterUsersavingrequest(newFilter)
                    setUsersavingrequestOrderUsersavingrequest(newOrder)
                }

                onUsersavingrequestSearchOrFilter()

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








