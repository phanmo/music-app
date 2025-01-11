package com.fpoly.pro226.music_app.ui.screen.forgot

import android.content.Context
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
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.data.source.network.fmusic_model.profile.PasswordBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
//import java.net.Authenticator
//import java.net.PasswordAuthentication
import java.util.Properties
import java.util.Random
//import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
//import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


data class ForgotUiState(
    val isLoading: Boolean = false,
    val isVerifyOTPSuccess: Boolean? = null,
    val isSendOTPSuccessfully: Boolean? = null,
    val isNewPasswordSuccessfully: Boolean? = null
)

open class ForgotViewModel(private val fMusicRepository: FMusicRepository) : ViewModel() {

    companion object {
        val MY_REPOSITORY_KEY = object : CreationExtras.Key<FMusicRepository> {}
        fun provideFactory(

        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = this[MY_REPOSITORY_KEY] as FMusicRepository
                ForgotViewModel(myRepository)
            }
        }
    }

    var forgotUiState by mutableStateOf(ForgotUiState())
        private set

    var email: String = ""
    var otpCode: String = ""
    val passwordBody = PasswordBody("", "", "")

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    private suspend fun showToast(message: String) {
        _toastEvent.emit(message)
    }

    fun sendOTP(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val errorMessage = validateInputs()
            if (errorMessage != null) {
                forgotUiState = forgotUiState.copy(
                    isLoading = false,
                )
                showToast(errorMessage)
            } else {
                try {
                    forgotUiState = forgotUiState.copy(isLoading = true)
                    val dataOTP = PreferencesManager(context).getOTP()
                    if (dataOTP?.isNotEmpty() == true) {
                        val emailOTP = dataOTP.split("-").firstOrNull()
                        val timeSendOTP = dataOTP.split("-").lastOrNull()
                        if (emailOTP != null && emailOTP == email
                            && timeSendOTP != null && timeSendOTP.toLong() + 10 * 60 * 1000 > System.currentTimeMillis()
                        ) {
                            Log.d("SendEmailTask", "001")
                            forgotUiState = forgotUiState.copy(isLoading = false, isSendOTPSuccessfully = true)
                            return@launch
                        }
                    }
                    Log.d("SendEmailTask", "002")
                    val host = "smtp.gmail.com" // SMTP server
                    val username = "kieumo54@gmail.com"
                    val password = "gtlc ffct oebx phck"

                    val otpCode: String = generateOTP() // Tạo mã OTP ngẫu nhiên
                    val otpTimestamp = System.currentTimeMillis() // Lấy thời gian gửi OTP

                    PreferencesManager(context).saveOTP("$email-$otpCode-$otpTimestamp")

                    // Cấu hình các thuộc tính gửi email qua SMTP
                    val properties = Properties()
                    properties["mail.smtp.auth"] = "true"
                    properties["mail.smtp.starttls.enable"] = "true"
                    properties["mail.smtp.host"] = host
                    properties["mail.smtp.port"] = "587"

                    val session: Session = Session.getInstance(properties, object : javax.mail.Authenticator() {
                        override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                            return javax.mail.PasswordAuthentication(username, password)
                        }
                    })
                    try {
                        val message: Message = MimeMessage(session)
                        message.setFrom(InternetAddress(username))
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
                        message.subject = "FMusic - Forgot password"
                        message.setText("OTP Code: $otpCode\nExpiry date: 3 minutes")

                        // Gửi email
                        Transport.send(message)

                        forgotUiState = forgotUiState.copy(isLoading = false, isSendOTPSuccessfully = true)
                    } catch (e: MessagingException) {
                        forgotUiState = forgotUiState.copy(
                            isLoading = false,
                            isSendOTPSuccessfully = false
                        )
                    }
                } catch (ex: Exception) {
                    forgotUiState = forgotUiState.copy(
                        isLoading = false,
                        isSendOTPSuccessfully = false
                    )
                }
            }
        }
    }

    fun checkOTP(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val errorMessage = validateOTPInputs()
            if (errorMessage != null) {
                forgotUiState = forgotUiState.copy(
                    isLoading = false,
                )
                showToast(errorMessage)
            } else {
                val dataOTP = PreferencesManager(context).getOTP()
                if (dataOTP?.isNotEmpty() == true) {
                    val timeSendOTP = dataOTP.split("-").lastOrNull()
                    val otpSave = dataOTP.split("-")[1]
                    if (otpSave != otpCode) {
                        showToast("OTP is incorrect")
                    } else if (timeSendOTP != null && timeSendOTP.toLong() + 10 * 60 * 1000 < System.currentTimeMillis()) {
                        showToast("OTP has expired")
                    } else {
                        forgotUiState = forgotUiState.copy(isLoading = false, isVerifyOTPSuccess = true)
                    }
                }
            }
        }
    }

    fun changePassword(context: Context) {
        if (passwordBody.isEmptyPassword()) {
            viewModelScope.launch {
                showToast("Password cannot be empty")
            }
            return
        }
        if (passwordBody.confirmNewPassword()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val dataOTP = PreferencesManager(context).getOTP()
                    var mEmail = ""
                    if (dataOTP?.isNotEmpty() == true) {
                        mEmail = dataOTP.split("-").firstOrNull() ?: ""
                    }
                    if (mEmail.isBlank()) {
                        showToast("Email information is required!")
                        return@launch
                    }
                    forgotUiState = forgotUiState.copy(isLoading = true)
                    val response = fMusicRepository.newPassword("$mEmail", passwordBody)
                    if (response.isSuccessful) {
                        response.body()?.let { res ->
                            forgotUiState = forgotUiState.copy(isLoading = false)
                            showToast("Change password successfully")
                        }
                    } else {
                        response.errorBody()?.let { res ->
                            forgotUiState = forgotUiState.copy(isLoading = false)
                            showToast("Email is not exists")
                        }
                    }
                } catch (e: Exception) {
                    forgotUiState = forgotUiState.copy(isLoading = false)
                }
            }
        } else {
            viewModelScope.launch {
                showToast("Password do not match !")
            }
        }
    }

    private fun generateOTP(): String {
        val random = Random()
        val otp = random.nextInt(9000) + 1000 // OTP có 4 chữ số
        return otp.toString()
    }

    private fun validateInputs(): String? {
        if (email.isBlank()) {
            return "Please fill Email"
        }
        return null
    }

    private fun validateOTPInputs(): String? {
        if (otpCode.isBlank()) {
            return "Please fill OTP"
        }
        return null
    }
}