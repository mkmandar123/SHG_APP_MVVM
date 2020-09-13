package com.digitaldetox.aww.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.digitaldetox.aww.di.auth.keys.MainViewModelKey
import com.digitaldetox.aww.ui.main.account.AccountViewModel
import com.digitaldetox.aww.ui.main.root.viewmodel.RootViewModel
import com.digitaldetox.aww.ui.main.create_subreddit.CreateSubredditViewModel
import com.digitaldetox.aww.ui.main.create_userloanrequest.CreateUserloanrequestViewModel
import com.digitaldetox.aww.viewmodels.MainViewModelFactory
import dagger.Binds
import com.digitaldetox.aww.ui.main.create_usersavingrequest.CreateUsersavingrequestViewModel

import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @MainViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accoutViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(RootViewModel::class)
    abstract fun bindRootViewModel(rootViewModel: RootViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(CreateSubredditViewModel::class)
    abstract fun bindCreateSubredditViewModel(createSubredditViewModel: CreateSubredditViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(CreateUserloanrequestViewModel::class)
    abstract fun bindCreateUserloanrequestViewModel(createUserloanrequestViewModel: CreateUserloanrequestViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(CreateUsersavingrequestViewModel::class)
    abstract fun bindCreateUsersavingrequestViewModel(createUsersavingrequestViewModel: CreateUsersavingrequestViewModel): ViewModel
}








