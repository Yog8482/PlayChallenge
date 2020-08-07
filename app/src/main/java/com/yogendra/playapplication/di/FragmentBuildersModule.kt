package com.yogendra.playapplication.di


import com.yogendra.playapplication.ui.details.DetailsFragment
import com.yogendra.playapplication.ui.home.HomeFragment
import com.yogendra.playapplication.ui.login.LoginFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): DetailsFragment
}
