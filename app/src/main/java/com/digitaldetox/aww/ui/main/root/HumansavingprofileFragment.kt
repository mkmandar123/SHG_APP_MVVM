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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.digitaldetox.aww.models.HumansavingprofileRoom
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.HUMANSAVINGPROFILE_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.HUMANSAVINGPROFILE_FILTER_USERNAME
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.HUMANSAVINGPROFILE_ORDER_ASC
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.HUMANSAVINGPROFILE_ORDER_DESC
import com.digitaldetox.aww.ui.main.root.state.ROOT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.viewmodel.*
import com.digitaldetox.aww.util.ErrorHandling.Companion.isPaginationDone
import com.digitaldetox.aww.util.StateMessageCallback
import com.digitaldetox.aww.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_humansavingprofile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import refreshFromCacheHumansavingprofile
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class HumansavingprofileFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions
) : BaseRootFragment(R.layout.fragment_humansavingprofile, viewModelFactory),
    HumansavingprofileListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: HumansavingprofileListAdapter
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


        viewState?.humansavingprofileFields?.humansavingprofileList = ArrayList()

        outState.putParcelable(
            ROOT_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.human_profile)
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)
        setupGlide()
        initRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCacheHumansavingprofile()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        humansavingprofile_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerStateHumansavingprofile(lmState)
        }
    }

    private fun subscribeObservers() {

        Log.d(TAG, "subscribeObservers: HUMANSAVINGPROFILE")
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            recyclerAdapter.notifyDataSetChanged()
            if (viewState != null) {
                recyclerAdapter.apply {
                    viewState.humansavingprofileFields.humansavingprofileList?.let {
                        preloadGlideImages(
                            requestManager = requestManager as RequestManager,
                            list = it
                        )
                    }

                    submitList(
                        humansavingprofileList = viewState.humansavingprofileFields.humansavingprofileList,
                        isQueryExhausted = viewState.humansavingprofileFields.isQueryExhausted ?: true
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
                    viewModel.setQueryExhaustedHumansavingprofile(true)
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

    private fun initSearchView(menu: Menu) {
        activity?.apply {
            val searchManager: SearchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search_humansavingprofile).actionView as SearchView
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
//                viewModel.setQueryHumansavingprofile(searchQuery).let {
//                    onHumansavingprofileSearchOrFilter()
//                }
            }
            true
        }


        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")
//            viewModel.setQueryHumansavingprofile(searchQuery).let {
//                onHumansavingprofileSearchOrFilter()
//            }

        }
    }

    private fun onHumansavingprofileSearchOrFilter() {
//        viewModel.loadFirstPageHumansavingprofile().let {
//            resetUI()
//        }
    }

    private fun resetUI() {
        humansavingprofile_recyclerview.smoothScrollToPosition(0)
        uiCommunicationListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun initRecyclerView() {

        humansavingprofile_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@HumansavingprofileFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = HumansavingprofileListAdapter(
                requestManager as RequestManager,
                this@HumansavingprofileFragment
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "HumansavingprofileFragment: attempting to load next page...")
//                        viewModel.nextPageHumansavingprofile()
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
        inflater.inflate(R.menu.search_menu_humansavingprofile, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_filter_settings_humansavingprofile -> {
                showFilterDialog()
                return true
            }

            R.id.action_create_humansavingprofile -> {
//                findNavController().navigate(R.id.action_humansavingprofileFragment_to_createHumansavingprofileFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemLongSelected(position: Int, item: HumansavingprofileRoom) {
//        viewModel.setHumansavingprofile(item)
//        try {
//
//            viewModel.setUpdatedTitleHumansavingprofile(item.title)
//            viewModel.setUpdatedBodyHumansavingprofile(item.description)
//            findNavController().navigate(R.id.action_humansavingprofileFragment_to_updateHumansavingprofileFragment)
//        } catch (e: Exception) {
//
//            Log.d(TAG, "Exception: ${e.message}")
//        }

    }

    override fun onItemSelected(position: Int, item: HumansavingprofileRoom) {
//        viewModel.setHumansavingprofile(item)

        val bundle = Bundle()

//        val arrayList = ArrayList<String>(item.members)
//
//        bundle.putStringArrayList(Constants.HUMANSAVINGPROFILE_MEMBERS_KEY, arrayList)
//        findNavController().navigate(R.id.action_humansavingprofileFragment_to_subuserFragment, bundle)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.humansavingprofileFields?.layoutManagerState?.let { lmState ->
            humansavingprofile_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        humansavingprofile_recyclerview.adapter = null
        requestManager = null
    }

    override fun onRefresh() {
        onHumansavingprofileSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    fun showFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_humansavingprofile_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilterHumansavingprofile()
            val order = viewModel.getOrderHumansavingprofile()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    HUMANSAVINGPROFILE_FILTER_DATE_UPDATED -> check(R.id.filter_date)
                    HUMANSAVINGPROFILE_FILTER_USERNAME -> check(R.id.filter_author)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    HUMANSAVINGPROFILE_ORDER_ASC -> check(R.id.filter_asc)
                    HUMANSAVINGPROFILE_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: apply filter.")

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_author -> HUMANSAVINGPROFILE_FILTER_USERNAME
                        R.id.filter_date -> HUMANSAVINGPROFILE_FILTER_DATE_UPDATED
                        else -> HUMANSAVINGPROFILE_FILTER_DATE_UPDATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
//                    saveFilterOptionsHumansavingprofile(newFilter, newOrder)
                    setHumansavingprofileFilterHumansavingprofile(newFilter)
                    setHumansavingprofileOrderHumansavingprofile(newOrder)
                }

                onHumansavingprofileSearchOrFilter()

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








