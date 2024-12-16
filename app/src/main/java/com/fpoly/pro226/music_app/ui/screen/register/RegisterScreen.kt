package com.fpoly.pro226.music_app.ui.screen.register

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
import androidx.compose.ui.tooling.preview.Preview
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
import com.fpoly.pro226.music_app.ui.theme.FFFFFF_87
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._121111
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import com.fpoly.pro226.music_app.ui.theme._DBE7E8

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    fMusicRepository: FMusicRepository,
) {
    val extras = MutableCreationExtras().apply {
        set(RegisterViewModel.MY_REPOSITORY_KEY, fMusicRepository)
    }
    val vm: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory(),
        extras = extras
    )
    val uiState = vm.registerUiState
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    LaunchedEffect(uiState) {
        vm.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(uiState.isRegisterSuccess) {
        if (uiState.isRegisterSuccess == true) {
            onRegisterSuccess()
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
            //        Image(
            //            painterResource(
            //                id = R.drawable.back
            //            ),
            //            contentDescription = "Logo",
            //            modifier = Modifier
            //                .width(24.dp)
            //                .height(24.dp)
            //                .fillMaxWidth()
            //                .clickable {
            //                    onBack()
            //                }
            //                .align(alignment = Alignment.Start)
            //        )
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
                text = "Create a new account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
            TextField(
                label = "Full name",
                isPassword = false,
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.profile),
                        contentDescription = "Email Icon",
                        tint = FFFFFF_87,
                        modifier = Modifier.size(16.dp)
                    )
                },
                onValueChange = {
                    vm.registerBody = vm.registerBody.copy(name = it)
                },
            )
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
                    vm.registerBody = vm.registerBody.copy(email = it)
                },
            )
            TextField(
                label = "Password",
                isPassword = true,
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.lock),
                        contentDescription = "Password Icon",
                        tint = FFFFFF_87,
                        modifier = Modifier.size(16.dp)
                    )
                },
                onValueChange = {
                    vm.registerBody = vm.registerBody.copy(password = it)
                },
            )
            TextField(
                label = "Confirm password",
                isPassword = true,
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.lock),
                        contentDescription = "Password Icon",
                        tint = FFFFFF_87,
                        modifier = Modifier.size(16.dp)
                    )
                },
                onValueChange = {
                    vm.confirmPass = it
                },
            )
            ButtonWithElevation(
                label = "Register",
                onclick = {
                    vm.register()
                })
            ContinueWith()
            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(1.dp, _DBE7E8),
                            shape = CircleShape
                        )
                        .padding(5.dp)
                        .clickable {}
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(1.dp, _DBE7E8),
                            shape = CircleShape
                        )
                        .padding(5.dp)
                        .clickable {}
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color.White,
                )
                Text(
                    text = "Log in",
                    fontSize = 16.sp,
                    color = _7CEEFF,
                    modifier = Modifier.clickable {
                        onBack()
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        if (uiState.isLoading) {
            LoadingDialog(onDismiss = { })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongContentPreview() {
    MusicAppTheme {
    }
}