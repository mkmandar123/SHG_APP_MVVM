package com.digitaldetox.aww.messagingmvvm.ui.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import com.digitaldetox.aww.messagingmvvm.models.GroupName
import com.digitaldetox.aww.messagingmvvm.models.User
import com.digitaldetox.aww.messagingmvvm.util.FirestoreUtil


class SharedViewModel : ViewModel() {


    private var friendsListMutableLiveData =
        MutableLiveData<List<User>>()
    private var usersCollectionRef: CollectionReference =
        FirestoreUtil.firestoreInstance.collection("users")



    fun loadMembers(group: GroupName): LiveData<List<User>> {

        val friendsIds = group.chat_members_in_group
        if (!friendsIds.isNullOrEmpty()) {
            val mFriendList = mutableListOf<User>()
            for (friendId in friendsIds) {
                usersCollectionRef.document(friendId).get()
                    .addOnSuccessListener { friendUser ->
                        val friend =
                            friendUser.toObject(User::class.java)
                        friend?.let { user -> mFriendList.add(user) }
                        friendsListMutableLiveData.value = mFriendList
                    }
            }
        } else {
            //user has no friends
            friendsListMutableLiveData.value = null
        }

        return friendsListMutableLiveData
    }

    fun loadFriends(loggedUser: User): MutableLiveData<List<User>> {

        val friendsIds = loggedUser.friends
        if (!friendsIds.isNullOrEmpty()) {
                    val mFriendList = mutableListOf<User>()
                    for (friendId in friendsIds) {
                        usersCollectionRef.document(friendId).get()
                            .addOnSuccessListener { friendUser ->
                            val friend =
                                friendUser.toObject(User::class.java)
                            friend?.let { user -> mFriendList.add(user) }
                            friendsListMutableLiveData.value = mFriendList
                        }
                    }
                } else {
            //user has no friends
                    friendsListMutableLiveData.value = null
                }

        return friendsListMutableLiveData
    }



}