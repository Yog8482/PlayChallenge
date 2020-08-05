package com.yogendra.playapplication.ui.login

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yogendra.playapplication.R
import com.yogendra.playapplication.ui.login.validation.LoginInputState

class LoginViewModel(private val context: Context) : ViewModel() {

    private val _loginDetailsState: MutableLiveData<LoginInputState> = MutableLiveData()
    val loginInputState: LiveData<LoginInputState>
        get() = _loginDetailsState


    fun loginDataChanged(email: String?, password: String?) {
        if (!isValidEmail(email)) {
            _loginDetailsState.setValue(LoginInputState(R.string.email_error, null))
        } else if (!isValidPassword(password)) {
            _loginDetailsState.setValue(LoginInputState(null, R.string.password_error))

        } else {
            _loginDetailsState.setValue(LoginInputState(true))

        }
    }

    fun isValidEmail(email: String?): Boolean {
        email?.let {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        return false
    }


    fun isValidPassword(password: String?): Boolean {
        password?.let {
            return password.length in 8..16
        }
        return false
    }


   /* fun getPasswordTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing.
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

//                loginDataChanged(s.toString(), getPasswordTextWatcher().onTextChanged().toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }


    fun getEmailTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing.
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

                isValidEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }*/


}