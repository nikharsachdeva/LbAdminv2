package com.laundrybuoy.admin.utils

import java.io.IOException

class NoInternetException(message : String) : IOException(message)
class APIException(message : String) : IOException(message)