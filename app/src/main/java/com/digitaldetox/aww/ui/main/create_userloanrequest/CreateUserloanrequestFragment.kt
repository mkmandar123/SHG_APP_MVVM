package com.digitaldetox.aww.ui.main.create_userloanrequest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.ui.AreYouSureCallback
import com.digitaldetox.aww.ui.main.create_userloanrequest.state.CREATE_USERLOANREQUEST_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.create_userloanrequest.state.CreateUserloanrequestStateEvent
import com.digitaldetox.aww.ui.main.create_userloanrequest.state.CreateUserloanrequestViewState
import com.digitaldetox.aww.util.*
import com.digitaldetox.aww.util.Constants.Companion.GALLERY_REQUEST_CODE
import com.digitaldetox.aww.util.SuccessHandling.Companion.SUCCESS_USERLOANREQUEST_CREATED
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_create_userloanrequest.*
import kotlinx.android.synthetic.main.fragment_create_usersavingrequest.*
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
class CreateUserloanrequestFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : BaseCreateUserloanrequestFragment(R.layout.fragment_create_userloanrequest, viewModelFactory) {

    private var albumId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { inState ->
            (inState[CREATE_USERLOANREQUEST_VIEW_STATE_BUNDLE_KEY] as CreateUserloanrequestViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
        albumId = "userloanrequest_create_album_id"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            CREATE_USERLOANREQUEST_VIEW_STATE_BUNDLE_KEY,
            viewModel.viewState.value
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.create_new_request)

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
            viewState.userloanrequestFields.let { newUserloanrequestFields ->
                if (userloanrequest_loanamount?.text.toString()
                        .isEmpty() || userloanrequest_loanamount?.text.toString() == "null"
                ) {

                }else{
                    setUserloanrequestProperties(
                        newUserloanrequestFields.newUserloanrequestTitle,
                        newUserloanrequestFields.newUserloanrequestBody,
                        newUserloanrequestFields.newUserloanrequestLoanamount
                    )
                }

            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (it.response.message.equals(SUCCESS_USERLOANREQUEST_CREATED)) {
                    viewModel.clearNewUserloanrequestFields()
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


    fun setUserloanrequestProperties(
        title: String? = "",
        body: String? = "",
        loanamount: Int? = -1
    ) {
        if (userloanrequest_loanamount?.text.toString()
                .isEmpty() || userloanrequest_loanamount?.text.toString() == "null"
        ) {

            userloanrequest_loanamount.setText("")

        }else{
            userloanrequest_title.setText(title)
            userloanrequest_body.setText(body)
            userloanrequest_loanamount.setText(loanamount.toString())
        }


    }

    private fun launchImageCrop(uri: Uri) {
        context?.let {
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    private fun publishNewUserloanrequest() {

        if (userloanrequest_loanamount?.text.toString()
                .isEmpty() || userloanrequest_loanamount?.text.toString() == "null"
        ) {
            Toast.makeText(context, getString(R.string.please_enter_all_required_fields), Toast.LENGTH_SHORT).show()
            uiCommunicationListener.hideSoftKeyboard()
        }else{
            viewModel.setStateEvent(
                CreateUserloanrequestStateEvent.CreateNewUserloanrequestEvent(
                    title = userloanrequest_title.text.toString(),
                    body = userloanrequest_body.text.toString(),
                    loanamount = userloanrequest_loanamount?.text.toString().toInt(),
                    authorsender = arguments?.getString(Constants.USERLOANREQUEST_AUTHORSENDER_KEY)!!,
                    subreddit = arguments?.getString(Constants.SUBREDDIT_MEMBERS_KEY)!!
                )
            )
            uiCommunicationListener.hideSoftKeyboard()
        }
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
        if (userloanrequest_loanamount?.text.toString()
                .isEmpty() || userloanrequest_loanamount?.text.toString() == "null"
        ) {


        }else{
            viewModel.setNewUserloanrequestFields(
                userloanrequest_title.text.toString(),
                userloanrequest_body.text.toString(),
                userloanrequest_loanamount.text.toString().toInt()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.publish_menu_create_userloanrequest, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.publish -> {
                val callback: AreYouSureCallback = object : AreYouSureCallback {

                    override fun proceed() {
                        publishNewUserloanrequest()
                        viewModel.clearNewUserloanrequestFields()
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










