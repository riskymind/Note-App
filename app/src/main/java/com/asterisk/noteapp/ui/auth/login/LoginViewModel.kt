package com.asterisk.noteapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.noteapp.data.remote.models.User
import com.asterisk.noteapp.data.repository.NoteRepo
import com.asterisk.noteapp.util.Constants
import com.asterisk.noteapp.util.Constants.IS_EMAIL_VALID
import com.asterisk.noteapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
) : ViewModel() {

    private val _loginState = MutableSharedFlow<Resource<String>>()
    val loginState: SharedFlow<Resource<String>> = _loginState

    fun loginUser(
        email: String,
        password: String,
    ) = viewModelScope.launch(Dispatchers.IO) {

        _loginState.emit(Resource.Loading())

        if (email.isEmpty() || password.isEmpty()) {
            _loginState.emit(Resource.Error("Some Fields are Empty"))
            return@launch
        }

        if (!IS_EMAIL_VALID(email)) {
            _loginState.emit(Resource.Error("Email is not valid"))
            return@launch
        }

        if (!Constants.IS_PASSWORD_VALID(password)) {
            _loginState.emit(Resource.Error("Password should be between ${Constants.MINIMUM_PASSWORD_LENGTH} and ${Constants.MAXIMUM_PASSWORD_LENGTH}"))
            return@launch
        }

        val user = User(email = email, password = password)
        _loginState.emit(noteRepo.login(user))


    }


}