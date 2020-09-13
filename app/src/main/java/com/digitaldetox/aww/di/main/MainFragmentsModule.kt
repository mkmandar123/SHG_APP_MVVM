package com.digitaldetox.aww.di.main

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.digitaldetox.aww.fragments.main.account.AccountFragmentFactory
import com.digitaldetox.aww.fragments.main.messagingmvvm.MessagingmvvmFragmentFactory
import com.digitaldetox.aww.fragments.main.root.RootFragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object MainFragmentsModule {

    @JvmStatic
    @MainScope
    @Provides
    @Named("AccountFragmentFactory")
    fun provideAccountFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return AccountFragmentFactory(
            viewModelFactory
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("MessagingmvvmFragmentFactory")
    fun provideMessagingmvvmFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return MessagingmvvmFragmentFactory(
            viewModelFactory
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named("RootFragmentFactory")
    fun provideRootFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory,
        requestOptions: RequestOptions,
        requestManager: RequestManager
    ): FragmentFactory {
        return RootFragmentFactory(
            viewModelFactory,
            requestOptions,
            requestManager
        )
    }


}