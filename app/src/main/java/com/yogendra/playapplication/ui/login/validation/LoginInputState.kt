package com.yogendra.playapplication.ui.login.validation


data class LoginInputState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
) {

    var emailError: Int? = usernameError
    var passError: Int? = passwordError
    var dataValid: Boolean? = isDataValid

    constructor(
        isDataValid: Boolean
    ) : this() {
        emailError = null
        passError = null
        dataValid = isDataValid
    }

}
