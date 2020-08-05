package com.yogendra.playapplication.ui.login.validation

import android.util.Patterns
import com.yogendra.playapplication.R


data class LoginInputDataWithState(
    val email: String? = null,
    val password: String? = null
) {
    var emailError: Int = checkEmail(email)
    var passError: Int = checkPassword(password)
    var dataValid: Boolean = (emailError == 0 && passError == 0)

    fun checkEmail(email: String?): Int {
        email?.let {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return R.string.email_error
            else
                return 0
        }
        return R.string.email_error
    }


    fun checkPassword(password: String?): Int {
        password?.let {
            if (password.length in 8..16)
                return 0
            else
                return R.string.password_error
        }
        return R.string.password_error

    }


}
