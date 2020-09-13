package com.digitaldetox.aww.messagingmvvm.fragments.add_members_to_group

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
import com.google.gson.Gson
import com.digitaldetox.aww.R
import com.digitaldetox.aww.databinding.ListOfFreindsToAddInGroupFragmentBinding
import com.digitaldetox.aww.messagingmvvm.models.GroupName
import com.digitaldetox.aww.messagingmvvm.models.User
import com.digitaldetox.aww.messagingmvvm.ui.mainActivity.SharedViewModel
import com.digitaldetox.aww.messagingmvvm.util.ClICKED_GROUP
import com.digitaldetox.aww.messagingmvvm.util.LOGGED_USER
import com.digitaldetox.aww.ui.UICommunicationListener
import kotlinx.android.synthetic.main.checkable_list_layout.view.*
import kotlinx.android.synthetic.main.list_of_freinds_to_add_in_group_fragment.*

private const val TAG = "AddMembersToGroupFragme"
class AddMembersToGroupFragment : Fragment() {
    lateinit var binding: ListOfFreindsToAddInGroupFragmentBinding
    lateinit var adapterr: FriendsAdapter
    var selectedItems: ArrayList<User>? = null
    var nonselectedItems: ArrayList<User>? = null
    private lateinit var clickedGroup: GroupName
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var viewmodel: AddMembersToGroupViewModel

    lateinit var uiCommunicationListener: UICommunicationListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.list_of_freinds_to_add_in_group_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProviders.of(this).get(AddMembersToGroupViewModel::class.java)
        uiCommunicationListener.expandAppBar()

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.add_members_to_group)

        //get user from shared preferences
        val mPrefs: SharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString(LOGGED_USER, null)
        val loggedUser: User = gson.fromJson(json, User::class.java)
        clickedGroup = gson.fromJson(arguments?.getString(ClICKED_GROUP), GroupName::class.java)
        selectedItems = java.util.ArrayList()
        nonselectedItems = java.util.ArrayList()
        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        //create adapter and handle recycle item click callback
        adapterr = FriendsAdapter(object :
            FriendsAdapter.ItemClickCallback {
            override fun onItemClicked(clickedUser: User, view: View) {
                btAddFreindsToGroup.visibility = View.VISIBLE

                val selectedItem = clickedUser
                Log.d("gghh", "selected Item is ${selectedItem.username}")
                if (selectedItems?.contains(selectedItem)!!) {
                    view.txt_title.isChecked = false
                    selectedItems
                        ?.remove(selectedItem)
                } else {
                    selectedItems?.add(
                        selectedItem
                    )
                    view.txt_title.isChecked = true

                }
            }
        })

        //load friends of logged in user and show in recycler
        sharedViewModel.loadFriends(loggedUser)
            .observe(viewLifecycleOwner, Observer { friendsList ->
                //hide loading
                binding.loadingImage.visibility = View.GONE
                if (friendsList != null) {
//                binding.friendsLayout.visibility = View.VISIBLE
//                binding.noFriendsLayout.visibility = View.GONE
                    showFriendsInRecycler(friendsList)
                } else {
//                  will handle later :)
                }

            })

        btAddFreindsToGroup.setOnClickListener {
            val newMembersIds = ArrayList<String>()
            for (i in selectedItems!!) {
                newMembersIds.add(i.uid.toString())
                d("gghh", "${i.username} are listed")
            }

            for (i in clickedGroup.chat_members_in_group!!) {
                newMembersIds.add(i)
                d("gghh", "${i} are listed old")
            }
            clickedGroup.chat_members_in_group = newMembersIds
            viewmodel.updateUserProfileForGroups(clickedGroup.group_name.toString(), newMembersIds)
            this.findNavController().popBackStack()
            //////////////////////  DONT FORGERT TO REMOVE THIS CODE AFTER IMPLEMENTING REFRESH OPTION ////////////////
            this.findNavController().popBackStack()
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
    }

    private fun showFriendsInRecycler(it: List<User>) {
        var newFreindlist = it.filterNot {
            clickedGroup.chat_members_in_group?.contains(it.uid)!!
        }
        adapterr.setDataSource(newFreindlist as List<User>)
        binding.recyclerWithCheckboxes.adapter = adapterr
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