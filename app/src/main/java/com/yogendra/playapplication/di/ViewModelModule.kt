package com.yogendra.playapplication.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yogendra.playapplication.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(UsersViewModel::class)
//    abstract fun bindUsersViewModel(viewModel: UsersViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(ProfileViewModel::class)
//    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
