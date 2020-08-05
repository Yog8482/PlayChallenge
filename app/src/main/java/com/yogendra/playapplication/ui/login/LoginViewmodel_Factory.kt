package com.yogendra.playapplication.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class LoginViewmodel_Factory internal constructor(
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Cannot create Instance for this class")
    }

}