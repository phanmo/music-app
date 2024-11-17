package com.fpoly.pro226.music_app.ui.screen.login

import android.util.Log
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
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.LoginResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.User
import kotlinx.coroutines.launch


data class LoginUiState(
    val isLoading: Boolean = false,
    val loginRes: LoginResponse? = null,
    val isLoginSuccess: Boolean? = null
)

class LoginViewModel(private val fMusicRepository: FMusicRepository) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = this[MY_REPOSITORY_KEY] as FMusicRepository
                LoginViewModel(myRepository)
            }
        }
    }

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    var user = User("", "")

    fun login() {
        Log.d("TAG", "login: called")
        viewModelScope.launch {
            try {
                loginUiState = loginUiState.copy(isLoading = true, isLoginSuccess = null)
                Log.d("TAG", "login: user = $user")
                val response = fMusicRepository.login(user)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        loginUiState = loginUiState.copy(
                            isLoading = false,
                            loginRes = res,
                            isLoginSuccess = res.status == 200
                        )
                    }
                } else {
                    loginUiState = loginUiState.copy(
                        isLoading = false,
                        isLoginSuccess = false
                    )
                }
            } catch (e: Exception) {
                Log.d("TAG", "login: e = $e")
                loginUiState = loginUiState.copy(isLoading = false, isLoginSuccess = false)
            }
        }

    }
}