package com.asterisk.noteapp.util

sealed class Resource<T>(val data:T? = null, val errorMessage: String? = null) {
    class Success<T>(data: T, errorMessage: String? = null): Resource<T>(data, errorMessage)
    class Error<T>(errorMessage: String, data: T? = null): Resource<T>(data, errorMessage)
    class Loading<T>: Resource<T>()
}
