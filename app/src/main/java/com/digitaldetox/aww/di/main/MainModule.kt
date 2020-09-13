package com.digitaldetox.aww.di.main

import com.digitaldetox.aww.api.main.OpenApiMainService
import com.digitaldetox.aww.persistence.AccountPropertiesDao
import com.digitaldetox.aww.persistence.SubredditDao
import com.digitaldetox.aww.persistence.UserloanrequestDao
import com.digitaldetox.aww.persistence.UsersavingrequestDao

import com.digitaldetox.aww.repository.main.AccountRepository
import com.digitaldetox.aww.repository.main.AccountRepositoryImpl
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.util.AppExecutors
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Module
object MainModule {


    @JvmStatic
    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        subredditDao: SubredditDao,
        userloanrequestDao: UserloanrequestDao,
        usersavingrequestDao: UsersavingrequestDao,

        sessionManager: SessionManager,
        executors: AppExecutors
    ): AccountRepository {
        return AccountRepositoryImpl(
            openApiMainService,
            accountPropertiesDao,
            subredditDao,
            userloanrequestDao,
            usersavingrequestDao,

            sessionManager,
            executors
        )
    }


}

















