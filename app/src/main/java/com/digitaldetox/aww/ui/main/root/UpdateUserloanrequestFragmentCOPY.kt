//package com.digitaldetox.aww.ui.main.root
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuInflater
//import android.view.MenuItem
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.bumptech.glide.RequestManager
//import com.digitaldetox.aww.R
//import com.digitaldetox.aww.ui.main.root.state.ROOT_VIEW_STATE_BUNDLE_KEY
//import com.digitaldetox.aww.ui.main.root.state.RootStateEvent.UpdateUsersavingrequestPostEvent
//import com.digitaldetox.aww.ui.main.root.state.RootViewState
//import com.digitaldetox.aww.ui.main.root.viewmodel.*
//import com.digitaldetox.aww.util.Constants.Companion.GALLERY_REQUEST_CODE
//import com.digitaldetox.aww.util.ErrorHandling.Companion.SOMETHING_WRONG_WITH_IMAGE
//import com.digitaldetox.aww.util.MessageType
//import com.digitaldetox.aww.util.Response
//import com.digitaldetox.aww.util.StateMessageCallback
//import com.digitaldetox.aww.util.SuccessHandling.Companion.SUCCESS_USERSAVINGREQUEST_UPDATED
//import com.digitaldetox.aww.util.UIComponentType
//import com.theartofdev.edmodo.cropper.CropImage
//import com.theartofdev.edmodo.cropper.CropImageView
//import kotlinx.android.synthetic.main.fragment_update_usersavingrequest.*
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import javax.inject.Inject
//
//@FlowPreview
//@ExperimentalCoroutinesApi
//class UpdateUsersavingrequestFragment
//@Inject
//constructor(
//    viewModelFactory: ViewModelProvider.Factory,
//    private val requestManager: RequestManager
//) : BaseRootFragment(R.layout.fragment_update_usersavingrequest, viewModelFactory) {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        savedInstanceState?.let { inState ->
//            (inState[ROOT_VIEW_STATE_BUNDLE_KEY] as RootViewState?)?.let { viewState ->
//                viewModel.setViewState(viewState)
//            }
//        }
//    }
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        val viewState = viewModel.viewState.value
//
//
//        viewState?.usersavingrequestFields?.usersavingrequestList = ArrayList()
//
//        outState.putParcelable(
//            ROOT_VIEW_STATE_BUNDLE_KEY,
//            viewState
//        )
//        super.onSaveInstanceState(outState)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
//        (activity as AppCompatActivity).supportActionBar?.title = "Update Saving Request"
//
//        setHasOptionsMenu(true)
//        subscribeObservers()
//        uiCommunicationListener.expandAppBar()
////        image_container.setOnClickListener {
////            if (uiCommunicationListener.isStoragePermissionGranted()) {
////                pickFromGallery()
////            }
////        }
//    }
//
//    private fun pickFromGallery() {
//        val intent = Intent(
//            Intent.ACTION_PICK,
//            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        intent.type = "image/*"
//        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivityForResult(intent, GALLERY_REQUEST_CODE)
//    }
//
//    private fun launchImageCrop(uri: Uri) {
//        context?.let {
//            CropImage.activity(uri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(it, this)
//        }
//    }
//
//    private fun showImageSelectionError() {
//        uiCommunicationListener.onResponseReceived(
//            response = Response(
//                message = SOMETHING_WRONG_WITH_IMAGE,
//                uiComponentType = UIComponentType.Dialog(),
//                messageType = MessageType.Error()
//            ),
//            stateMessageCallback = object : StateMessageCallback {
//                override fun removeMessageFromStack() {
//                    viewModel.clearStateMessage()
//                }
//            }
//        )
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//
//                GALLERY_REQUEST_CODE -> {
//                    data?.data?.let { uri ->
//                        activity?.let {
//                            launchImageCrop(uri)
//                        }
//                    } ?: showImageSelectionError()
//                }
//
//                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE")
//                    val result = CropImage.getActivityResult(data)
//                    val resultUri = result.uri
//                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE: uri: ${resultUri}")
//                    viewModel.setUpdatedUriUsersavingrequest(resultUri)
//                }
//
//                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
//                    Log.d(TAG, "CROP: ERROR")
//                    showImageSelectionError()
//                }
//            }
//        }
//    }
//
//    fun subscribeObservers() {
//
//        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
//            viewState.updatedUsersavingrequestFields.let { updatedUsersavingrequestFields ->
//
//                if (usersavingrequest_savingamount?.text.toString()
//                        .isEmpty() || usersavingrequest_savingamount?.text.toString() == "null"
//                ) {
//
//                }
//                setUsersavingrequestProperties(
//                    updatedUsersavingrequestFields.updatedUsersavingrequestTitle,
//                    updatedUsersavingrequestFields.updatedUsersavingrequestBody,
//                    updatedUsersavingrequestFields.updatedUsersavingrequestSavingamount
//                )
//
//            }
//        })
//
//        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
//            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
//        })
//
//        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
//
//            stateMessage?.let {
//
//                if (stateMessage.response.message.equals(SUCCESS_USERSAVINGREQUEST_UPDATED)) {
//                    viewModel.updateListItemUsersavingrequest()
//                }
//
//                uiCommunicationListener.onResponseReceived(
//                    response = it.response,
//                    stateMessageCallback = object : StateMessageCallback {
//                        override fun removeMessageFromStack() {
//                            viewModel.clearStateMessage()
//                        }
//                    }
//                )
//            }
//        })
//    }
//
//    fun setUsersavingrequestProperties(title: String?, body: String?, savingamount: Int?) {
//
////        if (usersavingrequest_savingamount?.text.toString()
////                .isEmpty() || usersavingrequest_savingamount?.text.toString() == "null"
////        ) {
////            usersavingrequest_savingamount.setText("")
////        }else{
//        usersavingrequest_title.setText(title)
//        usersavingrequest_body.setText(body)
//        usersavingrequest_savingamount.setText(savingamount.toString())
////        }
//
//
//    }
//
//    private fun saveChanges() {
//        if (usersavingrequest_savingamount?.text.toString()
//                .isEmpty() || usersavingrequest_savingamount?.text.toString() == "null"
//        ) {
//            Toast.makeText(context, "COol", Toast.LENGTH_SHORT).show()
//
//            uiCommunicationListener.hideSoftKeyboard()
//
//        } else {
//            viewModel.setStateEvent(
//                UpdateUsersavingrequestPostEvent(
//                    usersavingrequest_title.text.toString(),
//                    usersavingrequest_body.text.toString(),
//                    usersavingrequest_savingamount.text.toString().toInt()
//                )
//            )
//            uiCommunicationListener.hideSoftKeyboard()
//        }
//
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.update_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.save -> {
//                saveChanges()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (usersavingrequest_savingamount?.text.toString()
//                .isEmpty() || usersavingrequest_savingamount?.text.toString() == "null"
//        ) {
//
//        } else {
//            viewModel.setUpdatedTitleUsersavingrequest(usersavingrequest_title.text.toString())
//            viewModel.setUpdatedBodyUsersavingrequest(usersavingrequest_body.text.toString())
//            viewModel.setUpdatedSavingamountUsersavingrequest(
//                usersavingrequest_savingamount.text.toString().toInt()
//            )
//        }
//
//    }
//}
//
//
//
//
//
//
//
//
//
//
