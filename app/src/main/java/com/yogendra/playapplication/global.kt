package com.yogendra.playapplication

val LOGIN_URL = "https://256b758e-ed3e-400c-8ece-10dc78851f7a.mock.pstmn.io/";
const val DETAILS_URL = "https://hacker-news.firebaseio.com/v0/";
const val URL_ENDPOINT_1 = "topstories.json"
const val URL_ENDPOINT_2 = "item/{key}.json"

val DATABASE_NAME = "play-db"

//val INVALID_EMAIL_ERROR = "Not valid email"
//val INVALID_PASSWORD_ERROR = "Password must be at-least 8 chars long or at max 16"
val MOCK_EMAIL = "test@worldofplay.in"
val MOCK_PASSWORD = "Worldofplay@2020"
val SHARED_PREFS_NAME = "com.play.application_preferences"


enum class ProgressStatus {
    LOADING,
    NO_NETWORK,
    ERROR,
    COMPLTED
}

enum class LoginMockApiCall {
    SUCCESS,
    UN_AUTHORIZED,
    BAD_REQUEST
}