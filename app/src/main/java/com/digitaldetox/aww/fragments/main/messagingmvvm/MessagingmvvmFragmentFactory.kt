package com.digitaldetox.aww.fragments.main.messagingmvvm


import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.digitaldetox.aww.R
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.messagingmvvm.fragments.add_members_to_group.AddMembersToGroupFragment
import com.digitaldetox.aww.messagingmvvm.fragments.contacts.ContactsFragment
import com.digitaldetox.aww.messagingmvvm.fragments.create_group.CreateGroupFragment
import com.digitaldetox.aww.messagingmvvm.fragments.different_user_profile.DifferentUserProfileFragment
import com.digitaldetox.aww.messagingmvvm.fragments.fbLoginFragment.FacebookLoginFragment
import com.digitaldetox.aww.messagingmvvm.fragments.findUser.FindUserFragment
import com.digitaldetox.aww.messagingmvvm.fragments.group_info.GroupInfoFragment
import com.digitaldetox.aww.messagingmvvm.fragments.groupchat.RoomChatFragment
import com.digitaldetox.aww.messagingmvvm.fragments.home_group.HomeFragmentRoom
import com.digitaldetox.aww.messagingmvvm.fragments.home_one_to_one_chat.HomeFragment
import com.digitaldetox.aww.messagingmvvm.fragments.incoming_requests.IncomingRequestsFragment
import com.digitaldetox.aww.messagingmvvm.fragments.login.LoginMessagingFragment
import com.digitaldetox.aww.messagingmvvm.fragments.one_to_one_chat.ChatFragment
import com.digitaldetox.aww.messagingmvvm.fragments.profile.ProfileFragment
import com.digitaldetox.aww.messagingmvvm.fragments.register.SignupMessagingFragment
import com.digitaldetox.aww.ui.auth_messaging.BlankFragment
import com.digitaldetox.aww.ui.auth_messaging.LauncherMessagingFragment
import javax.inject.Inject

@MainScope
class MessagingmvvmFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            LoginMessagingFragment::class.java.name -> {
                LoginMessagingFragment()
            }

            LauncherMessagingFragment::class.java.name -> {
                LauncherMessagingFragment()
            }


            SignupMessagingFragment::class.java.name -> {
                SignupMessagingFragment()
            }

            BlankFragment::class.java.name -> {
                BlankFragment(viewModelFactory)
            }

            AddMembersToGroupFragment::class.java.name -> {
                AddMembersToGroupFragment()
            }


            ContactsFragment::class.java.name -> {
                ContactsFragment()
            }

            CreateGroupFragment::class.java.name -> {
                CreateGroupFragment()
            }

            DifferentUserProfileFragment::class.java.name -> {
                DifferentUserProfileFragment()
            }

            FacebookLoginFragment::class.java.name -> {
                FacebookLoginFragment()
            }

            FindUserFragment::class.java.name -> {
                FindUserFragment()
            }

            GroupInfoFragment::class.java.name -> {
                GroupInfoFragment()
            }

            RoomChatFragment::class.java.name -> {
                RoomChatFragment()
            }

            HomeFragmentRoom::class.java.name -> {
                HomeFragmentRoom()
            }

            HomeFragment::class.java.name -> {
                HomeFragment()
            }

            IncomingRequestsFragment::class.java.name -> {
                IncomingRequestsFragment()
            }

            ChatFragment::class.java.name -> {
                ChatFragment()
            }

            ProfileFragment::class.java.name -> {
                ProfileFragment()
            }


            else -> {
                LoginMessagingFragment()

            }
        }


}