package com.digitaldetox.aww.ui.main.root

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBREDDIT_FILTER_DATE_UPDATED
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBREDDIT_FILTER_USERNAME
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBREDDIT_ORDER_ASC
import com.digitaldetox.aww.persistence.RootQueryUtils.Companion.SUBREDDIT_ORDER_DESC
import com.digitaldetox.aww.ui.main.root.state.ROOT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.root.state.RootViewState
import com.digitaldetox.aww.ui.main.root.viewmodel.*
import com.digitaldetox.aww.util.Constants
import com.digitaldetox.aww.util.ErrorHandling.Companion.isPaginationDone
import com.digitaldetox.aww.util.StateMessageCallback
import com.digitaldetox.aww.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_subreddit.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import loadFirstPageSubreddit
import nextPageSubreddit
import refreshFromCacheSubreddit
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@FlowPreview
@ExperimentalCoroutinesApi
class SubredditFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions
) : BaseRootFragment(R.layout.fragment_subreddit, viewModelFactory),
    SubredditListAdapter.Interaction,  TextToSpeech.OnInitListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: SubredditListAdapter
    private var requestManager: RequestManager? = null

    var tts: TextToSpeech?=null


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


        viewState?.subredditFields?.subredditList = ArrayList()

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
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.SHG)
        setHasOptionsMenu(true)
        swipe_refresh.setOnRefreshListener(this)
        setupGlide()
        initRecyclerView()
        subscribeObservers()

        tts = TextToSpeech(activity, this)




    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFromCacheSubreddit()
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        subreddit_recyclerview.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerStateSubreddit(lmState)
        }
    }

    private fun subscribeObservers() {


        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            recyclerAdapter.notifyDataSetChanged()
            if (viewState != null) {
                recyclerAdapter.apply {
                    viewState.subredditFields.subredditList?.let {
                        preloadGlideImages(
                            requestManager = requestManager as RequestManager,
                            list = it
                        )
                    }

                    submitList(
                        subredditList = viewState.subredditFields.subredditList,
                        isQueryExhausted = viewState.subredditFields.isQueryExhausted ?: true
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
                    viewModel.setQueryExhaustedSubreddit(true)
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
            searchView = menu.findItem(R.id.action_search_subreddit).actionView as SearchView
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
                viewModel.setQuerySubreddit(searchQuery).let {
                    onSubredditSearchOrFilter()
                }
            }
            true
        }


        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")
            viewModel.setQuerySubreddit(searchQuery).let {
                onSubredditSearchOrFilter()
            }

        }
    }

    private fun onSubredditSearchOrFilter() {
        viewModel.loadFirstPageSubreddit().let {
            resetUI()
        }
    }

    private fun resetUI() {
        subreddit_recyclerview.smoothScrollToPosition(0)
        uiCommunicationListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun initRecyclerView() {

        subreddit_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@SubredditFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator)
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = SubredditListAdapter(
                requestManager as RequestManager,
                this@SubredditFragment
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "SubredditFragment: attempting to load next page...")
                        viewModel.nextPageSubreddit()
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
        inflater.inflate(R.menu.search_menu_subreddit, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_filter_settings_subreddit -> {
                showFilterDialog()
                return true
            }

            R.id.action_create_subreddit -> {
                findNavController().navigate(R.id.action_subredditFragment_to_createSubredditFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemLongSelected(position: Int, item: SubredditRoom) {

//        Toast.makeText(context, "LONG PRESS", Toast.LENGTH_SHORT).show()
//        viewModel.setSubreddit(item)
//        try {
//
//            viewModel.setUpdatedTitleSubreddit(item.title)
//            viewModel.setUpdatedBodySubreddit(item.description)
////            findNavController().navigate(R.id.action_subredditFragment_to_updateSubredditFragment)
//            findNavController().navigate(R.id.action_subredditFragment_to_humanloanprofileFragment)
//        } catch (e: Exception) {
//
//            Log.d(TAG, "Exception: ${e.message}")
//        }

    }

    override fun onLoanButtonSelected(position: Int, item: SubredditRoom) {
        viewModel.setSubreddit(item)
        try {

            viewModel.setUpdatedTitleSubreddit(item.title)
            viewModel.setUpdatedBodySubreddit(item.description)
//            findNavController().navigate(R.id.action_subredditFragment_to_updateSubredditFragment)
            findNavController().navigate(R.id.action_subredditFragment_to_humanloanprofileFragment)
        } catch (e: Exception) {

            Log.d(TAG, "Exception: ${e.message}")
        }
    }


    override fun onSavingButtonSelected(position: Int, item: SubredditRoom) {
        viewModel.setSubreddit(item)
        try {

            viewModel.setUpdatedTitleSubreddit(item.title)
            viewModel.setUpdatedBodySubreddit(item.description)
//            findNavController().navigate(R.id.action_subredditFragment_to_updateSubredditFragment)
            findNavController().navigate(R.id.action_subredditFragment_to_humansavingprofileFragment)
        } catch (e: Exception) {

            Log.d(TAG, "Exception: ${e.message}")
        }
    }

    override fun onAllSpeech(speechString: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts?.speak(speechString, TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts?.speak(speechString, TextToSpeech.QUEUE_FLUSH, null);
        }

    }


    override fun onUpdateSubredditButtonSelected(position: Int, item: SubredditRoom) {

        viewModel.setSubreddit(item)
        try {
            viewModel.setUpdatedTitleSubreddit(item.title)
            viewModel.setUpdatedBodySubreddit(item.description)
            findNavController().navigate(R.id.action_subredditFragment_to_updateSubredditFragment)
        } catch (e: Exception) {

            Log.d(TAG, "Exception: ${e.message}")
        }
    }


    override fun onItemSelected(position: Int, item: SubredditRoom) {
        viewModel.setSubreddit(item)

        val bundle = Bundle()

        val arrayList = ArrayList<String>(item.members)

        val subredditName = item.title

        bundle.putStringArrayList(Constants.SUBREDDIT_MEMBERS_KEY, arrayList)
        bundle.putString(Constants.SUBREDDIT_NAME_KEY, subredditName)
        findNavController().navigate(R.id.action_subredditFragment_to_subuserFragment, bundle)
    }

    override fun restoreListPosition() {
        viewModel.viewState.value?.subredditFields?.layoutManagerState?.let { lmState ->
            subreddit_recyclerview?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        subreddit_recyclerview.adapter = null
        requestManager = null
    }

    override fun onRefresh() {
        onSubredditSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    fun showFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_subreddit_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilterSubreddit()
            val order = viewModel.getOrderSubreddit()

            view.findViewById<RadioGroup>(R.id.filter_group).apply {
                when (filter) {
                    SUBREDDIT_FILTER_DATE_UPDATED -> check(R.id.filter_date)
                    SUBREDDIT_FILTER_USERNAME -> check(R.id.filter_author)
                }
            }

            view.findViewById<RadioGroup>(R.id.order_group).apply {
                when (order) {
                    SUBREDDIT_ORDER_ASC -> check(R.id.filter_asc)
                    SUBREDDIT_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: apply filter.")

                val newFilter =
                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
                        R.id.filter_author -> SUBREDDIT_FILTER_USERNAME
                        R.id.filter_date -> SUBREDDIT_FILTER_DATE_UPDATED
                        else -> SUBREDDIT_FILTER_DATE_UPDATED
                    }

                val newOrder =
                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
                        R.id.filter_desc -> "-"
                        else -> ""
                    }

                viewModel.apply {
                    saveFilterOptionsSubreddit(newFilter, newOrder)
                    setSubredditFilterSubreddit(newFilter)
                    setSubredditOrderSubreddit(newOrder)
                }

                onSubredditSearchOrFilter()

                dialog.dismiss()
            }

            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
                Log.d(TAG, "FilterDialog: cancelling filter.")
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.forLanguageTag("hin"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }


    }


}








