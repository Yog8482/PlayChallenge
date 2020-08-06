package com.yogendra.playapplication.utilities

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String="No internet connection") : IOException(message)