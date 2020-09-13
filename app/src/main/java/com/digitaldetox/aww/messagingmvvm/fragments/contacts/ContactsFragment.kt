package com.digitaldetox.aww.messagingmvvm.fragments.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ourchat.ui.contacts.ContactsViewModel
import com.google.gson.Gson
import com.digitaldetox.aww.R
import com.digitaldetox.aww.databinding.ContactsFragmentBinding
import com.digitaldetox.aww.messagingmvvm.models.User
import com.digitaldetox.aww.messagingmvvm.ui.mainActivity.SharedViewModel
import com.digitaldetox.aww.messagingmvvm.util.CLICKED_USER
import com.digitaldetox.aww.messagingmvvm.util.LOGGED_USER
import com.digitaldetox.aww.ui.UICommunicationListener

const val USERNAME = "username"
const val PROFILE_PICTURE = "profile_picture_url"
const val UID = "uid"
private const val TAG = "ContactsFragment"
class ContactsFragment : Fragment() {

    lateinit var binding: ContactsFragmentBinding
    lateinit var uiCommunicationListener: UICommunicationListener

    companion object {
        fun newInstance() =
            ContactsFragment()
    }

    private lateinit var viewModel: ContactsViewModel
    private lateinit var adapter: ContactsAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.contacts_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        uiCommunicationListener.expandAppBar()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.contacts)

        //get user from shared preferences
        val mPrefs: SharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString(LOGGED_USER, null)
        val loggedUser: User = gson.fromJson(json, User::class.java)

        //on contact click move to chat fragment
        adapter = ContactsAdapter(object :
            ContactsAdapter.ItemClickCallback {
            override fun onItemClicked(clickedUser: User) {

                println("ContactsFragment.onItemClicked:${clickedUser.username}")
                //turn clicked user to json
                val clickedUser = gson.toJson(clickedUser)

                findNavController().navigate(
                    R.id.action_contactsFragment_to_chatFragment, bundleOf(
                        CLICKED_USER to clickedUser
                    )
                )
            }
        })

        sharedViewModel.loadFriends(loggedUser).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                //user has friends
                showFriends(it)
            } else {
                //user has no friends
                showEmptyLayout()
            }
        })


    }


    private fun showFriends(it: List<User>) {
        binding.noFriendsLayout.visibility = View.GONE
        adapter.submitList(it)
        adapter.usersList = it
        binding.contactsRecycler.adapter = adapter
    }

    private fun showEmptyLayout() {
        binding.noFriendsLayout.visibility = View.VISIBLE
        binding.addFriendsButton.setOnClickListener { findNavController().navigate(R.id.action_contactsFragment_to_findUserFragment) }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)


        inflater.inflate(R.menu.search_menu_messaging, menu)

        //do filtering when i type in search or click search
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
               override fun onQueryTextSubmit(queryString: String?): Boolean {
                   adapter.filter.filter(queryString)
                   return false
               }

               override fun onQueryTextChange(queryString: String?): Boolean {
                   adapter.filter.filter(queryString)
                   if (queryString != null) {
                       adapter.onChange(queryString)
                   }

                   return false
               }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_search -> {
            println("MainActivity.onOptionsItemSelected:${item.title}")
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }

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
