package com.fpoly.pro226.music_app.ui.screen.profile

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.components.di.AppContainer
import com.fpoly.pro226.music_app.data.source.local.PreferencesManager
import com.fpoly.pro226.music_app.ui.components.LoadingDialog
import com.fpoly.pro226.music_app.ui.screen.login.ButtonWithElevation
import com.fpoly.pro226.music_app.ui.screen.login.TextField
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import java.io.File

@Composable
fun EditProfileScreen(onBack: () -> Unit, appContainer: AppContainer) {

    val extras = MutableCreationExtras().apply {
        set(EditProfileViewModel.MY_REPOSITORY_KEY, appContainer.fMusicRepository)
    }
    val context = LocalContext.current

    val vm: EditProfileViewModel = viewModel(
        factory = EditProfileViewModel.provideFactory(PreferencesManager(context).getUserId()),
        extras = extras,
    )
    val uiState = vm.editProfileUiState

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(Unit) {
        vm.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF102B2D),
                        Color(0xFF000000),
                        Color(0xFF000000),
                    )
                )
            )
            .fillMaxHeight()
    ) {
        if (uiState.isLoading) {
            LoadingDialog(onDismiss = { })
        } else {
            uiState.userInfo?.let {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    onBack()
                                }
                        )
                        Text(
                            text = "Edit Profile",
                            color = _00C2CB,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(vertical = 40.dp)
                            .size(120.dp)
                            .border(
                                border = BorderStroke(2.dp, Color.Transparent),
                                shape = CircleShape
                            )
                            .padding(2.dp)
                            .align(alignment = Alignment.CenterHorizontally)

                    ) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .fillMaxSize()
                                    .clickable {
                                        launcher.launch("image/*")
                                    }
                            )
                        } else {
                            AsyncImage(
                                contentScale = ContentScale.Crop,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .clickable {
                                        launcher.launch("image/*")
                                    },
                                model = uiState.userInfo.avatar,
                                placeholder = painterResource(R.drawable.ic_app),
                                error = painterResource(R.drawable.ic_app),

                                )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                            contentDescription = "Image Search",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    TextField(
                        value = uiState.userInfo.username,
                        label = "Username",
                        isPassword = false,
                        enable = false,
                        onValueChange = {
                        },
                    )
                    TextField(
                        value = uiState.userInfo.email,
                        label = "Email",
                        isPassword = false,
                        onValueChange = {
                            vm.profileBody.email = it
                        },
                    )
                    TextField(
                        value = uiState.userInfo.name,
                        label = "Full name",
                        isPassword = false,
                        onValueChange = {
                            vm.profileBody.name = it
                        },
                    )
                    TextField(
                        value = uiState.userInfo.getBirthdayByString(),
                        label = "Birthday",
                        isPassword = false,
                        onValueChange = {
                            vm.profileBody.birthday = it
                        },
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ButtonWithElevation(label = "Save", onclick = {
                        val imageFile = uriToFile(context, selectedImageUri)
                        vm.updateProfile(imageFile) { userInfo ->
                            PreferencesManager(context).saveUser(userInfo)
                        }
                    })
                }
            }
        }


    }
}

fun uriToFile(context: Context, uri: Uri?): File? {
    if (uri == null) {
        return null
    }
    val fileName = "${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, fileName)

    try {
        val inputStream = uri.let { context.contentResolver.openInputStream(it) }
        val outputStream = file.outputStream()

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    MusicAppTheme {
//        EditProfileScreen() {}
    }
}