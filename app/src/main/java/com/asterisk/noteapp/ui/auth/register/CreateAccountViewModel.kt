package com.asterisk.noteapp.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.noteapp.data.remote.models.User
import com.asterisk.noteapp.data.repository.NoteRepo
import com.asterisk.noteapp.util.Constants.IS_EMAIL_VALID
import com.asterisk.noteapp.util.Constants.IS_PASSWORD_VALID
import com.asterisk.noteapp.util.Constants.MAXIMUM_PASSWORD_LENGTH
import com.asterisk.noteapp.util.Constants.MINIMUM_PASSWORD_LENGTH
import com.asterisk.noteapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
) : ViewModel() {

    private val _registerState = MutableSharedFlow<Resource<String>>()
    val registerState: SharedFlow<Resource<String>> = _registerState

    fun createUser(
        name: String,
        email: String,
        password: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        _registerState.emit(Resource.Loading())
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _registerState.emit(Resource.Error("Some fields are empty"))
            return@launch
        }

        if (!IS_EMAIL_VALID(email)) {
            _registerState.emit(Resource.Error("Email is inValid"))
            return@launch
        }

        if (!IS_PASSWORD_VALID(password)) {
            _registerState.emit(Resource.Error("Password should be between $MINIMUM_PASSWORD_LENGTH and $MAXIMUM_PASSWORD_LENGTH"))
            return@launch
        }

        val newUser = User(name, email, password)

        _registerState.emit(noteRepo.createUser(newUser))
    }




//    private fun isEmailValid(email: String): Boolean {
//        val regex =
//            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
//        val pattern = Pattern.compile(regex)
//        return email.isNotEmpty() && pattern.matcher(email).matches()
//    }
//
//    private fun isPasswordValid(password: String): Boolean {
//        return password.length in MINIMUM_PASSWORD_LENGTH..MAXIMUM_PASSWORD_LENGTH
//    }

}