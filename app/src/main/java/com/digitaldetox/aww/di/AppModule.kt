package com.digitaldetox.aww.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.digitaldetox.aww.R
import com.digitaldetox.aww.api.main.OpenApiMainService
import com.digitaldetox.aww.persistence.*
import com.digitaldetox.aww.persistence.AppDatabase.Companion.DATABASE_NAME
import com.digitaldetox.aww.repository.main.*
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.util.AppExecutors
import com.digitaldetox.aww.util.Constants
import com.digitaldetox.aww.util.PreferenceKeys
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@FlowPreview
@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreferences(
        application: Application
    ): SharedPreferences {
        return application
            .getSharedPreferences(
                PreferenceKeys.APP_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPrefsEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitBuilder(gsonBuilder: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAuthTokenDao(db: AppDatabase): AuthTokenDao {
        return db.getAuthTokenDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideAccountPropertiesDao(db: AppDatabase): AccountPropertiesDao {
        return db.getAccountPropertiesDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSubredditDao(db: AppDatabase): SubredditDao {
        return db.getSubredditroomDao()
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideHumansavingprofileDao(db: AppDatabase): HumansavingprofileDao {
        return db.getHumansavingprofileroomDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideHumanloanprofileDao(db: AppDatabase): HumanloanprofileDao {
        return db.getHumanloanprofileroomDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUsersavingrequestRoomDao(db: AppDatabase): UsersavingrequestDao {
        return db.getUsersavingrequestroomDao()
    }




    @JvmStatic
    @Singleton
    @Provides
    fun provideCreateUsersavingrequestRepository(
        openApiMainService: OpenApiMainService,
        usersavingrequestDao: UsersavingrequestDao,
        sessionManager: SessionManager,
        executors: AppExecutors
    ): CreateUsersavingrequestRepository {
        return CreateUsersavingrequestRepositoryImpl(
            openApiMainService,
            usersavingrequestDao,
            sessionManager,
            executors
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideUserloanrequestRoomDao(db: AppDatabase): UserloanrequestDao {
        return db.getUserloanrequestroomDao()
    }




    @JvmStatic
    @Singleton
    @Provides
    fun provideCreateUserloanrequestRepository(
        openApiMainService: OpenApiMainService,
        userloanrequestDao: UserloanrequestDao,
        sessionManager: SessionManager,
        executors: AppExecutors
    ): CreateUserloanrequestRepository {
        return CreateUserloanrequestRepositoryImpl(
            openApiMainService,
            userloanrequestDao,
            sessionManager,
            executors
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRootRepository(
        openApiMainService: OpenApiMainService,
        subredditDao: SubredditDao,
        humanloanprofileDao: HumanloanprofileDao,
        humansavingprofileDao: HumansavingprofileDao,

        userloanrequestDao: UserloanrequestDao,
        usersavingrequestDao: UsersavingrequestDao,

        sessionManager: SessionManager,
        executors: AppExecutors
    ): RootRepository {
        return RootRepositoryImpl(
            openApiMainService, subredditDao, humanloanprofileDao, humansavingprofileDao
            , userloanrequestDao,usersavingrequestDao,
             sessionManager, executors
        )
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideCreateSubredditRepository(
        openApiMainService: OpenApiMainService,
        subredditDao: SubredditDao,
        sessionManager: SessionManager,
        executors: AppExecutors
    ): CreateSubredditRepository {
        return CreateSubredditRepositoryImpl(
            openApiMainService,
            subredditDao,
            sessionManager,
            executors
        )
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideInstance(
        application: Application,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }


}