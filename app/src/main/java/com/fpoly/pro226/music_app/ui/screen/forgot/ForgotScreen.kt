package com.fpoly.pro226.music_app.ui.screen.forgot

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.ui.components.LoadingDialog
import com.fpoly.pro226.music_app.ui.screen.login.ButtonWithElevation
import com.fpoly.pro226.music_app.ui.screen.login.ContinueWith
import com.fpoly.pro226.music_app.ui.screen.login.TextField
import com.fpoly.pro226.music_app.ui.screen.register.RegisterViewModel
import com.fpoly.pro226.music_app.ui.theme.FFFFFF_87
import com.fpoly.pro226.music_app.ui.theme._121111
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import com.fpoly.pro226.music_app.ui.theme._DBE7E8

@Composable
fun ForgotScreen(
    fMusicRepository: FMusicRepository,
    onNextVerifyOTP: () -> Unit
) {
    val extras = MutableCreationExtras().apply {
        set(ForgotViewModel.MY_REPOSITORY_KEY, fMusicRepository)
    }
    val vm: ForgotViewModel = viewModel(
        factory = ForgotViewModel.provideFactory(),
        extras = extras
    )
    val uiState = vm.forgotUiState
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    LaunchedEffect(uiState) {
        vm.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(uiState.isSendOTPSuccessfully) {
        if (uiState.isSendOTPSuccessfully == true) {
            onNextVerifyOTP()
        } else if (uiState.isSendOTPSuccessfully == false) {
            Toast.makeText(context, "Error sending OTP", Toast.LENGTH_SHORT).show()
        }
    }
    Box {
        Column(
            modifier = Modifier
                .background(_121111)
                .padding(16.dp)
                .fillMaxHeight()
                .verticalScroll(scrollState)

        ) {
            Image(
                painterResource(
                    id = R.drawable.ic_app
                ),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = "Forgot password",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = "Email",
                isPassword = false,
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.mail),
                        contentDescription = "Email Icon",
                        tint = FFFFFF_87,
                        modifier = Modifier.size(16.dp)
                    )
                },
                onValueChange = {
                    vm.email = it
                },
            )

            Spacer(modifier = Modifier.height(20.dp))
            ButtonWithElevation(
                label = "Send OTP",
                onclick = {
                    vm.sendOTP(context)
                }
            )
            // Spacer(modifier = Modifier.height(20.dp))
        }
        if (uiState.isLoading) {
            LoadingDialog(onDismiss = { })
        }
    }
}