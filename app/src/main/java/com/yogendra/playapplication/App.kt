package com.yogendra.playapplication

import android.app.Activity
import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.yogendra.playapplication.di.AppInjector
import com.yogendra.playapplication.utilities.ThemeManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
//        initTheme()
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> =
        dispatchingAndroidInjector as AndroidInjector<Any>

    private fun initTheme() {
        val preferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        ThemeManager.applyTheme(preferences.getString("preference_key_theme", "")!!)
    }
}