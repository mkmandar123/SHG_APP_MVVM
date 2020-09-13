package com.digitaldetox.aww

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.digitaldetox.aww.di.AppComponent
import com.digitaldetox.aww.di.DaggerAppComponent
import com.digitaldetox.aww.di.auth.AuthComponent
import com.digitaldetox.aww.di.main.MainComponent
import com.digitaldetox.aww.work.DaggerWorkerFactory
import net.gotev.uploadservice.UploadService
import net.gotev.uploadservice.okhttp.OkHttpStack
import javax.inject.Inject

class BaseApplication : Application(){

    lateinit var appComponent: AppComponent

    private var authComponent: AuthComponent? = null

    private var mainComponent: MainComponent? = null

    @Inject
    lateinit var workerFactory: DaggerWorkerFactory

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        WorkManager.initialize(this, Configuration.Builder().run {
            setWorkerFactory(appComponent.workerFactory())
                .build()
        })

        setUpUploadService()
    }

    fun releaseMainComponent(){
        mainComponent = null
    }

    fun mainComponent(): MainComponent {
        if(mainComponent == null){
            mainComponent = appComponent.mainComponent().create()
        }
        return mainComponent as MainComponent
    }

    fun releaseAuthComponent(){
        authComponent = null
    }

    fun authComponent(): AuthComponent {
        if(authComponent == null){
            authComponent = appComponent.authComponent().create()
        }
        return authComponent as AuthComponent
    }

    fun initAppComponent(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    private fun setUpUploadService() {
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID
        UploadService.HTTP_STACK = OkHttpStack()
        UploadService.PROGRESS_REPORT_INTERVAL = 500
    }


}

