package com.digitaldetox.aww.ui.main.create_subreddit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.ui.AreYouSureCallback
import com.digitaldetox.aww.ui.main.create_subreddit.state.CREATE_SUBREDDIT_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.create_subreddit.state.CreateSubredditStateEvent
import com.digitaldetox.aww.ui.main.create_subreddit.state.CreateSubredditViewState
import com.digitaldetox.aww.util.Constants.Companion.GALLERY_REQUEST_CODE
import com.digitaldetox.aww.util.MessageType
import com.digitaldetox.aww.util.Response
import com.digitaldetox.aww.util.StateMessageCallback
import com.digitaldetox.aww.util.SuccessHandling.Companion.SUCCESS_SUBREDDIT_CREATED
import com.digitaldetox.aww.util.UIComponentType
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_create_subreddit.*
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
class CreateSubredditFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : BaseCreateSubredditFragment(R.layout.fragment_create_subreddit, viewModelFactory) {

    private var albumId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { inState ->
            (inState[CREATE_SUBREDDIT_VIEW_STATE_BUNDLE_KEY] as CreateSubredditViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
        albumId = "subreddit_create_album_id"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            CREATE_SUBREDDIT_VIEW_STATE_BUNDLE_KEY,
            viewModel.viewState.value
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.create_new_shg)
        setHasOptionsMenu(true)
        uiCommunicationListener.expandAppBar()

        subscribeObservers()
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.subredditFields.let { newSubredditFields ->
                setSubredditProperties(
                    newSubredditFields.newSubredditTitle,
                    newSubredditFields.newSubredditBody
                )
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (it.response.message.equals(SUCCESS_SUBREDDIT_CREATED)) {
                    viewModel.clearNewSubredditFields()
                }
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })

        viewModel.load(albumId)
    }


    fun setSubredditProperties(
        title: String? = "",
        body: String? = ""
    ) {
        subreddit_title.setText(title)
        subreddit_body.setText(body)
    }

    private fun launchImageCrop(uri: Uri) {
        context?.let {
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    private fun publishNewSubreddit() {
        viewModel.setStateEvent(
            CreateSubredditStateEvent.CreateNewSubredditEvent(
                subreddit_title.text.toString(),
                subreddit_body.text.toString()
            )
        )
        uiCommunicationListener.hideSoftKeyboard()

    }

    fun showErrorDialog(errorMessage: String) {
        uiCommunicationListener.onResponseReceived(
            response = Response(
                message = errorMessage,
                uiComponentType = UIComponentType.Dialog(),
                messageType = MessageType.Error()
            ),
            stateMessageCallback = object : StateMessageCallback {
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.setNewSubredditFields(
            subreddit_title.text.toString(),
            subreddit_body.text.toString()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.publish_menu_create_subreddit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.publish -> {
                val callback: AreYouSureCallback = object : AreYouSureCallback {

                    override fun proceed() {
                        publishNewSubreddit()
                        viewModel.clearNewSubredditFields()
                    }

                    override fun cancel() {
                        // ignore
                    }

                }
                uiCommunicationListener.onResponseReceived(
                    response = Response(
                        message = getString(R.string.are_you_sure_publish),
                        uiComponentType = UIComponentType.AreYouSureDialog(callback),
                        messageType = MessageType.Info()
                    ),
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}










