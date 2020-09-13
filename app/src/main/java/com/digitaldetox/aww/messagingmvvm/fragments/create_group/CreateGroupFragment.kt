package com.digitaldetox.aww.messagingmvvm.fragments.create_group

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.digitaldetox.aww.R
import com.digitaldetox.aww.databinding.CreategroupFragmentBinding
import com.digitaldetox.aww.messagingmvvm.fragments.groupchat.gson
import com.digitaldetox.aww.messagingmvvm.models.GroupName
import com.digitaldetox.aww.messagingmvvm.util.ErrorMessage
import com.digitaldetox.aww.messagingmvvm.util.LoadState
import com.digitaldetox.aww.ui.UICommunicationListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.issue_layout.view.*
import java.util.regex.Pattern

private const val TAG = "CreateGroupFragment"
class CreateGroupFragment : Fragment() {
    private lateinit var binding: CreategroupFragmentBinding
    private lateinit var pattern: Pattern
    lateinit var uiCommunicationListener: UICommunicationListener

    companion object {
        fun newInstance() = CreateGroupFragment()
    }

    private lateinit var viewModel: CreateGroupViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.creategroup_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateGroupViewModel::class.java)
        uiCommunicationListener.expandAppBar()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.create_group)

        //regex pattern to check email format
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$"
        pattern = Pattern.compile(emailRegex)

        // activity?.navView?.visibility = View.GONE

        //handle register click
        binding.createGroupButton.setOnClickListener {
            createGroup()
        }


        //hide issue layout on x icon click
        binding.issueLayout.cancelImage.setOnClickListener {
            binding.issueLayout.visibility = View.GONE
        }

        //show proper loading/error ui
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadState.LOADING -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                    binding.issueLayout.visibility = View.GONE
                }
                LoadState.SUCCESS -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.issueLayout.visibility = View.GONE
                }
                LoadState.FAILURE -> {
                    binding.loadingLayout.visibility = View.GONE
                    binding.issueLayout.visibility = View.VISIBLE
                    binding.issueLayout.textViewIssue.text = ErrorMessage.errorMessage
                }

            }
        })


        //sign up on keyboard done click when focus is on passwordEditText
        binding.newgroupDescriptionEditText.setOnEditorActionListener { _, actionId, _ ->
            createGroup()
            true
        }

    }

    private fun createGroup() {

        binding.groupName.isErrorEnabled = false
        binding.description.isErrorEnabled = false


        if (binding.groupName.editText!!.text.length < 4) {
            binding.groupName.error = "Group name should be at least 4 characters"
            return
        }


        //check if email is empty_box or wrong format
        if (binding.description.editText!!.text.isEmpty()) {
            binding.description.error = "Please Enter Group Description."
            return

        }


        //email and pass are matching requirements now we can register to firebase auth
        viewModel.createdGroupFlag.observe(viewLifecycleOwner, Observer { flag ->

            if (flag){
                this.findNavController().popBackStack()
            }
        })

        //get user data
        viewModel.loggedUserMutableLiveData.observe(viewLifecycleOwner, Observer { loggedUser ->
            //save logged user data in shared pref to use in other fragments
            val mPrefs: SharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
            val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
            val json = gson.toJson(loggedUser)
            prefsEditor.putString("loggedUser", json)
            prefsEditor.apply()
            var groupName = GroupName()
            groupName.description = binding.description.editText!!.text.toString()
            groupName.group_name = binding.groupName.editText!!.text.toString()
            groupName.chat_members_in_group = listOf(loggedUser.uid.toString())


            if (groupName != null) {
                viewModel.createGroup(
                    loggedUser,
                    groupName
                )

            } else
                d("gghh", "failed")
        })
    }
    @SuppressLint("LongLogTag")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }

    }

}



