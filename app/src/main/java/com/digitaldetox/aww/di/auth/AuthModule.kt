package com.digitaldetox.aww.di.auth

import android.content.SharedPreferences
import com.digitaldetox.aww.api.auth.OpenApiAuthService
import com.digitaldetox.aww.persistence.AccountPropertiesDao
import com.digitaldetox.aww.persistence.AuthTokenDao
import com.digitaldetox.aww.repository.auth.AuthRepository
import com.digitaldetox.aww.repository.auth.AuthRepositoryImpl
import com.digitaldetox.aww.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@FlowPreview
@Module
object AuthModule{

    @JvmStatic
    @AuthScope
    @Provides
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService {
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @JvmStatic
    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService,
        preferences: SharedPreferences,
        editor: SharedPreferences.Editor
        ): AuthRepository {
        return AuthRepositoryImpl(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager,
            preferences,
            editor
        )
    }


}









