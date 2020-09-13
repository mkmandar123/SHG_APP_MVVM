package com.digitaldetox.aww.ui.main.create_usersavingrequest

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
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
import com.digitaldetox.aww.ui.main.create_usersavingrequest.state.CREATE_USERSAVINGREQUEST_VIEW_STATE_BUNDLE_KEY
import com.digitaldetox.aww.ui.main.create_usersavingrequest.state.CreateUsersavingrequestStateEvent
import com.digitaldetox.aww.ui.main.create_usersavingrequest.state.CreateUsersavingrequestViewState
import com.digitaldetox.aww.util.*
import com.digitaldetox.aww.util.Constants.Companion.GALLERY_REQUEST_CODE
import com.digitaldetox.aww.util.SuccessHandling.Companion.SUCCESS_USERSAVINGREQUEST_CREATED
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_create_usersavingrequest.*
import kotlinx.coroutines.FlowPreview
import java.util.*
import javax.inject.Inject

@FlowPreview
class CreateUsersavingrequestFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : BaseCreateUsersavingrequestFragment(
    R.layout.fragment_create_usersavingrequest,
    viewModelFactory
) {

    private var albumId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { inState ->
            (inState[CREATE_USERSAVINGREQUEST_VIEW_STATE_BUNDLE_KEY] as CreateUsersavingrequestViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
        albumId = "usersavingrequest_create_album_id"
    }

    companion object {
        private const val REQUEST_CODE_STT_TITLE = 1
        private const val REQUEST_CODE_STT_BODY = 2
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            CREATE_USERSAVINGREQUEST_VIEW_STATE_BUNDLE_KEY,
            viewModel.viewState.value
        )
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.create_new_request)

        subscribeObservers()

        speak_usersavingrequest_body.setOnClickListener {
            val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            // Language model defines the purpose, there are special models for other use cases, like search.
            sttIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            // Adding an extra language, you can use any language from the Locale class.
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            // Text that shows up on the Speech input prompt.
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            try {
                // Start the intent for a result, and pass in our request code.
                startActivityForResult(sttIntent, REQUEST_CODE_STT_BODY)
            } catch (e: ActivityNotFoundException) {
                // Handling error when the service is not available.
                e.printStackTrace()
                Toast.makeText(context, "Your device does not support STT.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        speak_usersavingrequest_title.setOnClickListener {
            val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            // Language model defines the purpose, there are special models for other use cases, like search.
            sttIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            // Adding an extra language, you can use any language from the Locale class.
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            // Text that shows up on the Speech input prompt.
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            try {
                // Start the intent for a result, and pass in our request code.
                startActivityForResult(sttIntent, REQUEST_CODE_STT_TITLE)
            } catch (e: ActivityNotFoundException) {
                // Handling error when the service is not available.
                e.printStackTrace()
                Toast.makeText(context, "Your device does not support STT.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // Handle the result for our request code.
            REQUEST_CODE_STT_BODY -> {
                // Safety checks to ensure data is available.
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Retrieve the result array.
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // Ensure result array is not null or empty to avoid errors.
                    if (!result.isNullOrEmpty()) {
                        // Recognized text is in the first position.
                        val recognizedText = result[0]
                        // Do what you want with the recognized text.
                        usersavingrequest_body.setText(recognizedText)
                    }
                }
            }

            REQUEST_CODE_STT_TITLE -> {
                // Safety checks to ensure data is available.
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Retrieve the result array.
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // Ensure result array is not null or empty to avoid errors.
                    if (!result.isNullOrEmpty()) {
                        // Recognized text is in the first position.
                        val recognizedText = result[0]
                        // Do what you want with the recognized text.
                        usersavingrequest_title.setText(recognizedText)
                    }
                }
            }
        }
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
            viewState.usersavingrequestFields.let { newUsersavingrequestFields ->

                if (usersavingrequest_savingamount?.text.toString()
                        .isEmpty() && usersavingrequest_savingamount?.text.toString() == "null"
                ) {

                } else {
                    setUsersavingrequestProperties(
                        newUsersavingrequestFields.newUsersavingrequestTitle,
                        newUsersavingrequestFields.newUsersavingrequestBody,
                        newUsersavingrequestFields.newUsersavingrequestSavingamount
                    )
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                if (it.response.message.equals(SUCCESS_USERSAVINGREQUEST_CREATED)) {
                    viewModel.clearNewUsersavingrequestFields()
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


    fun setUsersavingrequestProperties(
        title: String? = "",
        body: String? = "",
        savingamount: Int? = -1
    ) {
        if (usersavingrequest_savingamount?.text.toString()
                .isEmpty() || usersavingrequest_savingamount?.text.toString() == "null"
        ) {
            usersavingrequest_savingamount.setText("")

        } else {
            usersavingrequest_title.setText(title)
            usersavingrequest_body.setText(body)
            usersavingrequest_savingamount.setText(savingamount.toString())
        }
    }

    private fun launchImageCrop(uri: Uri) {
        context?.let {
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    private fun publishNewUsersavingrequest() {

        if (usersavingrequest_savingamount?.text.toString()
                .isEmpty() || usersavingrequest_savingamount?.text.toString() == "null"
        ) {
            Toast.makeText(
                context,
                getString(R.string.please_enter_all_required_fields),
                Toast.LENGTH_SHORT
            ).show()
            uiCommunicationListener.hideSoftKeyboard()

        } else {
            viewModel.setStateEvent(
                CreateUsersavingrequestStateEvent.CreateNewUsersavingrequestEvent(
                    title = usersavingrequest_title.text.toString(),
                    body = usersavingrequest_body.text.toString(),
                    savingamount = usersavingrequest_savingamount?.text.toString().toInt(),
                    authorsender = arguments?.getString(Constants.USERSAVINGREQUEST_AUTHORSENDER_KEY)!!,
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



        if (usersavingrequest_savingamount?.text.toString()
                .isEmpty() || usersavingrequest_savingamount?.text.toString() == "null"
        ) {
        } else {


            viewModel.setNewUsersavingrequestFields(
                usersavingrequest_title.text.toString(),
                usersavingrequest_body.text.toString(),
                usersavingrequest_savingamount.text.toString().toInt()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.publish_menu_create_usersavingrequest, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.publish -> {
                val callback: AreYouSureCallback = object : AreYouSureCallback {

                    override fun proceed() {
                        publishNewUsersavingrequest()
                        viewModel.clearNewUsersavingrequestFields()
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










