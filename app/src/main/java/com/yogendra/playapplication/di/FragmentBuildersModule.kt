package com.yogendra.playapplication.di


import com.yogendra.playapplication.ui.login.LoginFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeArticleFragment(): LoginFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeUsersFragment(): UsersFragment
//
//    @ContributesAndroidInjector
//    abstract fun contributeProfileFragment(): ProfileFragment
}
