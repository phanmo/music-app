package com.fpoly.pro226.music_app.ui.screen.profile

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
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.login.UserInfo
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.PasswordBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.ProfileBody
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.ProfileResponse
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.toAvatarPart
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.toMultipartParams
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File


data class EditProfileUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = null
)

class EditProfileViewModel(
    private val fMusicRepository: FMusicRepository,
    private val userId: String? = null
) : ViewModel() {
    companion object {

        // Define a custom key for your dependency
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}

        fun provideFactory(userId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val fMusicRepo = this[MY_REPOSITORY_KEY] as FMusicRepository

                EditProfileViewModel(
                    fMusicRepository = fMusicRepo,
                    userId = userId
                )
            }
        }
    }

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent


    val profileBody = ProfileBody("", "", "", "")

    val passwordBody = PasswordBody("", "", "")

    private var fetchProfile: Job? = null
    var editProfileUiState by mutableStateOf(EditProfileUiState())
        private set

    init {
        userId?.let {
            getProfile(it)
        }
    }


    suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun changePassword() {
        if (passwordBody.isEmptyPassword()) {
            viewModelScope.launch {
                showToast("Password cannot be empty")
            }
            return
        }
        if (passwordBody.confirmNewPassword()) {
            viewModelScope.launch {
                try {
                    editProfileUiState = editProfileUiState.copy(isLoading = true)
                    val response = fMusicRepository.changePassword("$userId", passwordBody)
                    if (response.isSuccessful) {
                        response.body()?.let { res ->
                            editProfileUiState = editProfileUiState.copy(isLoading = false)
                            showToast("Change password successfully")
                        }
                    } else {
                        response.errorBody()?.let { res ->
                            editProfileUiState = editProfileUiState.copy(isLoading = false)
                            showToast("Current password incorrect")
                        }
                    }
                } catch (e: Exception) {
                    editProfileUiState = editProfileUiState.copy(isLoading = false)
                }
            }
        } else {
            viewModelScope.launch {
                showToast("Password do not match !")
            }
        }
    }

    private fun getProfile(id: String) {
        fetchProfile?.cancel()
        fetchProfile = viewModelScope.launch {
            try {
                editProfileUiState = editProfileUiState.copy(isLoading = true)
                val response = fMusicRepository.getProfile(id)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        editProfileUiState =
                            editProfileUiState.copy(userInfo = res.data, isLoading = false)
                        profileBody.apply {
                            birthday = res.data.getBirthdayByString()
                            name = res.data.name
                            email = res.data.email
                            username = res.data.username
                        }
                    }
                }
            } catch (e: Exception) {
                editProfileUiState = editProfileUiState.copy(isLoading = false)
            } finally {
                fetchProfile = null
            }
        }
    }

    fun updateProfile(
        avatar: File?,
        onSuccess: (UserInfo) -> Unit
    ) {
        fetchProfile?.cancel()
        fetchProfile = viewModelScope.launch {
            try {
                editProfileUiState = editProfileUiState.copy(isLoading = true)
                val response: Response<ProfileResponse> = if (avatar != null) {
                    fMusicRepository.updateProfileAll(
                        userId = editProfileUiState.userInfo?._id ?: "",
                        data = profileBody.toMultipartParams(),
                        avatar = profileBody.toAvatarPart(avatar)
                    )
                } else {
                    fMusicRepository.updateProfile(
                        userId = editProfileUiState.userInfo?._id ?: "",
                        data = profileBody.toMultipartParams(),
                    )
                }

                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        editProfileUiState =
                            editProfileUiState.copy(userInfo = res.data, isLoading = false)
                        showToast("Updated profile successfully")
                        onSuccess(res.data)
                    }
                } else {
                    showToast("Updated profile failed")
                }
            } catch (e: Exception) {
                editProfileUiState = editProfileUiState.copy(isLoading = false)
                showToast("Updated profile failed")
            } finally {
                fetchProfile = null
            }
        }
    }
}