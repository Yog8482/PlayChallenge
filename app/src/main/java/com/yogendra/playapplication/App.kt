package com.yogendra.playapplication

import android.app.Activity
import android.app.Application
import com.yogendra.playapplication.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> =
        dispatchingAndroidInjector as AndroidInjector<Any>


}