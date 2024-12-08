package com.fpoly.pro226.music_app.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.register.RegisterBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.register.RegisterResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val registerRes: RegisterResponse? = null,
    val isRegisterSuccess: Boolean? = null,
)

class RegisterViewModel(private val fMusicRepository: FMusicRepository) : ViewModel() {
    companion object {
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}
        fun provideFactory(

        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = this[MY_REPOSITORY_KEY] as FMusicRepository
                RegisterViewModel(myRepository)
            }
        }
    }

    var registerUiState by mutableStateOf(RegisterUiState())
        private set
    var registerBody = RegisterBody("", "", "")
    var confirmPass = ""
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent
    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    private fun validateInputs(): String? {
        if (registerBody.email.isBlank()
            || registerBody.name.isBlank()
            || registerBody.password.isBlank()
            || confirmPass.isBlank()
        ) {
            return "Please fill all fields"
        }
        if (!registerBody.email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
            return "Invalid email"
        }
        if (registerBody.password.length < 8) {
            return "Password must be at least 8 characters"
        }
        if (!registerBody.password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z]).+$"))) {
            return "Password must include lowercase and uppercase letters"
        }
        if (registerBody.password != confirmPass) {
            return "Password not match"
        }
        return null
    }

    fun register() {
        viewModelScope.launch {
            val errorMessage = validateInputs()
            if (errorMessage != null) {
                registerUiState = registerUiState.copy(
                    isLoading = false,
                    isRegisterSuccess = false,
                )
                showToast(errorMessage)
            } else {
                try {
                    registerUiState =
                        registerUiState.copy(isLoading = true, isRegisterSuccess = null)
                    val response = fMusicRepository.register(registerBody)
                    if (response.isSuccessful) {
                        response.body()?.let { res ->
                            registerUiState = registerUiState.copy(
                                isLoading = false,
                                registerRes = res,
                                isRegisterSuccess = res.status == 200
                            )
                            showToast("Register success")
                        }
                    } else {
                        if (response.code() == 400) {
                            registerUiState =
                                registerUiState.copy(isLoading = false, isRegisterSuccess = false)
                            showToast("Email already exists")
                        } else {
                            registerUiState =
                                registerUiState.copy(isLoading = false, isRegisterSuccess = false)
                            showToast("Register failed")
                        }
                    }
                } catch (e: Exception) {
                    registerUiState =
                        registerUiState.copy(isLoading = false, isRegisterSuccess = false)
                }
            }
        }
    }
}