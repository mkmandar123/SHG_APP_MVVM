package com.digitaldetox.aww.fragments.main.root

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.digitaldetox.aww.di.main.MainScope
import com.digitaldetox.aww.ui.main.create_subreddit.CreateSubredditFragment
import com.digitaldetox.aww.ui.main.create_userloanrequest.CreateUserloanrequestFragment
import com.digitaldetox.aww.ui.main.create_usersavingrequest.CreateUsersavingrequestFragment

import com.digitaldetox.aww.ui.main.root.*
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@MainScope
class RootFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestOptions: RequestOptions,
    private val requestManager: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            HumansavingprofileFragment::class.java.name -> {
                HumansavingprofileFragment(viewModelFactory, requestOptions)
            }


            HumanloanprofileFragment::class.java.name -> {
                HumanloanprofileFragment(viewModelFactory, requestOptions)
            }


            SubredditFragment::class.java.name -> {
                SubredditFragment(viewModelFactory, requestOptions)
            }

            ViewSubredditFragment::class.java.name -> {
                ViewSubredditFragment(viewModelFactory, requestManager)
            }

            UpdateSubredditFragment::class.java.name -> {
                UpdateSubredditFragment(viewModelFactory, requestManager)
            }

            CreateSubredditFragment::class.java.name -> {
                CreateSubredditFragment(viewModelFactory, requestManager)
            }

            SubuserFragment::class.java.name -> {
                SubuserFragment(viewModelFactory, requestOptions)
            }

            UserloanrequestFragment::class.java.name -> {
                UserloanrequestFragment(viewModelFactory, requestOptions)
            }

            ViewUserloanrequestFragment::class.java.name -> {
                ViewUserloanrequestFragment(viewModelFactory, requestManager)
            }

            UpdateUserloanrequestFragment::class.java.name -> {
                UpdateUserloanrequestFragment(viewModelFactory, requestManager)
            }

            CreateUserloanrequestFragment::class.java.name -> {
                CreateUserloanrequestFragment(viewModelFactory, requestManager)
            }


            SubuserFragment::class.java.name -> {
                SubuserFragment(viewModelFactory, requestOptions)
            }

            UsersavingrequestFragment::class.java.name -> {
                UsersavingrequestFragment(viewModelFactory, requestOptions)
            }

            ViewUsersavingrequestFragment::class.java.name -> {
                ViewUsersavingrequestFragment(viewModelFactory, requestManager)
            }

            UpdateUsersavingrequestFragment::class.java.name -> {
                UpdateUsersavingrequestFragment(viewModelFactory, requestManager)
            }

            CreateUsersavingrequestFragment::class.java.name -> {
                CreateUsersavingrequestFragment(viewModelFactory, requestManager)
            }

            else -> {
                SubredditFragment(viewModelFactory, requestOptions)
            }
        }


}