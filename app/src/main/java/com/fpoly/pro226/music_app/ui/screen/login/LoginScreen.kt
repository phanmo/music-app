package com.fpoly.pro226.music_app.ui.screen.login

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.data.repositories.FMusicRepository
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.ui.components.LoadingDialog
import com.fpoly.pro226.music_app.ui.theme.FFFFFF_87
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import com.fpoly.pro226.music_app.ui.theme._121111
import com.fpoly.pro226.music_app.ui.theme._1E1E1E_85
import com.fpoly.pro226.music_app.ui.theme._446266
import com.fpoly.pro226.music_app.ui.theme._7CEEFF
import com.fpoly.pro226.music_app.ui.theme._DBE7E8

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onClickRegister: () -> Unit,
    fMusicRepository: FMusicRepository
) {
    val extras = MutableCreationExtras().apply {
        set(LoginViewModel.MY_REPOSITORY_KEY, fMusicRepository)
    }
    val vm: LoginViewModel = viewModel(
        factory = LoginViewModel.provideFactory(),
        extras = extras
    )

    val uiState = vm.loginUiState

    val rememberMeState = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        if (uiState.isLoginSuccess == true) {
            val preferencesManager = PreferencesManager(context)
            uiState.loginRes?.token?.let { accessToken ->
                preferencesManager.saveUser(uiState.loginRes.data)
                preferencesManager.saveAccessToken(accessToken)
                preferencesManager.saveUser(uiState.loginRes.data._id)
            }
            onLoginSuccess()
        } else if (uiState.isLoginSuccess == false) {
            Toast.makeText(context, "Email or Password is incorrect !", Toast.LENGTH_SHORT).show()
        }
    }
    Box {
        Column(
            modifier = Modifier
                .background(_121111)
                .fillMaxHeight()
                .padding(16.dp)
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
            //                .align(alignment = Alignment.Start)
            //        )
            Image(
                painterResource(
                    id = R.drawable.ic_app
                ),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = "Login to your account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
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
                onValueChange = { email ->
                    vm.user = vm.user.copy(email = email)
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
                onValueChange = { password ->
                    vm.user = vm.user.copy(password = password)
                },
            )
            RememberMeCheckBox(rememberMeState)
            ButtonWithElevation(label = "Log In", onclick = {
                vm.login()
            })
            Text(
                text = "Forgot password ?", color = Color.Cyan, fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clickable { }
            )
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
                    color = Color.White,
                )
                Text(
                    text = "Register",
                    fontSize = 16.sp,
                    color = _7CEEFF,
                    modifier = Modifier.clickable {
                        onClickRegister()
                    }
                )
            }
        }
        if (vm.loginUiState.isLoading) {
            LoadingDialog(onDismiss = { })
        }
    }
}

@Composable
fun TextField(
    label: String,
    isPassword: Boolean,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable() (() -> Unit)? = null,
    enable: Boolean? = true,
    value: String = ""
) {
    val textValue = remember { mutableStateOf(TextFieldValue(value)) }
    val passwordVisible = remember { mutableStateOf(false) }
    val visualTransformation = if (isPassword && !passwordVisible.value) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }
    val keyboardOptions = if (isPassword) {
        KeyboardOptions(keyboardType = KeyboardType.Password)
    } else {
        KeyboardOptions.Default
    }

    OutlinedTextField(
        enabled = enable ?: true,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            Log.d("mo", "TextField: $it")
            onValueChange(it.text)
        },
        label = {
            Text(
                text = label, style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    color = _00C2CB,
                    fontSize = 16.sp,
                )
            )
        },

        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = _1E1E1E_85,
            focusedContainerColor = _1E1E1E_85,
            unfocusedIndicatorColor = _DBE7E8,
            focusedIndicatorColor = _DBE7E8,
            unfocusedLabelColor = Color.White,
            focusedLabelColor = Color.White,
            disabledContainerColor = _1E1E1E_85,
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (enable == true) {
                Color.White
            } else {
                _446266
            },
            fontWeight = FontWeight.SemiBold
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible.value)
                    painterResource(id = R.drawable.eye)
                else painterResource(id = R.drawable.eye_off)
                IconButton(onClick = {
                    passwordVisible.value = !passwordVisible.value
                }) {
                    Icon(
                        painter = image,
                        contentDescription = "",
                        tint = FFFFFF_87,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun RememberMeCheckBox(rememberMeState: MutableState<Boolean>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = rememberMeState.value,
            onCheckedChange = {
                rememberMeState.value = it
            },
            colors = CheckboxDefaults.colors(
                uncheckedColor = _00C2CB,
                checkedColor = _00C2CB,
                checkmarkColor = Color.White
            )
        )
        Text(
            text = "Remember me ?",
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight(600)
            )
        )
    }
}

@Composable
fun ButtonWithElevation(label: String, onclick: () -> Unit) {
    Button(
        onClick = onclick,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(_00C2CB),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp)
            .height(48.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp,
        )

    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ContinueWith() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color.Gray)
        )
        Text(
            text = "or continue with",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SongContentPreview() {
    MusicAppTheme {
//        LoginScreen(
//            onLoginSuccess = {},
//            onClickRegister = {},
//            fMusicRepository = object : FMusicRepository {
//
//            })
    }
}