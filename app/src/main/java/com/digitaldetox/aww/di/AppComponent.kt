package com.digitaldetox.aww.di

import android.app.Application
import com.digitaldetox.aww.di.auth.AuthComponent
import com.digitaldetox.aww.di.main.MainComponent
import com.digitaldetox.aww.di.main.keys.WorkerKey
import com.digitaldetox.aww.session.SessionManager
import com.digitaldetox.aww.ui.BaseActivity
import com.digitaldetox.aww.upload.ImageUploadWorkerSubredditCreate
import com.digitaldetox.aww.upload.ImageUploadWorkerUserloanrequestCreate
import com.digitaldetox.aww.upload.ImageUploadWorkerUsersavingrequestCreate

import com.digitaldetox.aww.work.DaggerWorkerFactory
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class,
        WorkerBindingModuleSubredditCreate::class,
        WorkerBindingModuleUserloanrequestCreate::class,
        WorkerBindingModuleUsersavingrequestCreate::class

    ]
)
interface AppComponent  {

    val sessionManager: SessionManager
    fun workerFactory(): DaggerWorkerFactory

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)

    fun authComponent(): AuthComponent.Factory

    fun mainComponent(): MainComponent.Factory

}

@Module
interface WorkerBindingModuleSubredditCreate {
    @Binds
    @IntoMap
    @WorkerKey(ImageUploadWorkerSubredditCreate::class)
    fun bindImageUploaderWorkerSubredditCreate(factory: ImageUploadWorkerSubredditCreate.Factory): DaggerWorkerFactory.ChildWorkerFactory
}

@Module
interface WorkerBindingModuleUserloanrequestCreate {
    @Binds
    @IntoMap
    @WorkerKey(ImageUploadWorkerUserloanrequestCreate::class)
    fun bindImageUploaderWorkerUserloanrequestCreate(factory: ImageUploadWorkerUserloanrequestCreate.Factory): DaggerWorkerFactory.ChildWorkerFactory
}



@Module
interface WorkerBindingModuleUsersavingrequestCreate {
    @Binds
    @IntoMap
    @WorkerKey(ImageUploadWorkerUsersavingrequestCreate::class)
    fun bindImageUploaderWorkerUsersavingrequestCreate(factory: ImageUploadWorkerUsersavingrequestCreate.Factory): DaggerWorkerFactory.ChildWorkerFactory
}



