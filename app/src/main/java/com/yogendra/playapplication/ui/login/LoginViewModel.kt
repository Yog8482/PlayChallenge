package com.yogendra.playapplication.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yogendra.playapplication.ui.login.validation.LoginInputDataWithState

class LoginViewModel() : ViewModel() {

    private val _loginDetailsState: MutableLiveData<LoginInputDataWithState> = MutableLiveData()
    val loginInputState: LiveData<LoginInputDataWithState>
        get() = _loginDetailsState


    fun loginDataChanged(email: String?, password: String?) {
        _loginDetailsState.setValue(LoginInputDataWithState(email, password))
    }


}