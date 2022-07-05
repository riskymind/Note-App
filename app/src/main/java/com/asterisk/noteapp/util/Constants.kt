package com.asterisk.noteapp.util

import java.util.regex.Pattern

object Constants {
    const val JWT_TOKEN_KEY = "JWT_TOKEN_KEY"
    const val NAME_KEY = "NAME_KEY"
    const val EMAIL_KEY = "EMAIL_KEY"
    const val BASE_URL = "https://thawing-beyond-42860.herokuapp.com"
    const val API_VERSION = "/v1"
    const val MINIMUM_PASSWORD_LENGTH = 4
    const val MAXIMUM_PASSWORD_LENGTH = 10

    fun IS_EMAIL_VALID(email: String): Boolean {
        val regex =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        val pattern = Pattern.compile(regex)
        return email.isNotEmpty() && pattern.matcher(email).matches()
    }

    fun IS_PASSWORD_VALID(password: String): Boolean {
        return password.length in MINIMUM_PASSWORD_LENGTH..MAXIMUM_PASSWORD_LENGTH
    }
}