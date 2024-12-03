package com.fpoly.pro226.music_app.ui.screen.profile

import android.net.Uri
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.fpoly.pro226.music_app.R
import com.fpoly.pro226.music_app.ui.screen.login.ButtonWithElevation
import com.fpoly.pro226.music_app.ui.screen.login.TextField
import com.fpoly.pro226.music_app.ui.theme.MusicAppTheme
import com.fpoly.pro226.music_app.ui.theme._00C2CB
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDateTime

@Composable
fun EditProfileScreen(onBack: () -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }


    Column(
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
                Image(
                    painter = painterResource(id = R.drawable.matcha),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }
            Image(
                painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                contentDescription = "Image Search",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        TextField(
            label = "Username",
            isPassword = false,
            onValueChange = {},
        )
        TextField(
            label = "Email",
            isPassword = false,
            onValueChange = {
            },
        )
        TextField(
            label = "Full name",
            isPassword = false,
            onValueChange = {},
        )
        TextField(
            label = "Birthday",
            isPassword = false,
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithElevation(label = "Save", onclick = {
            if (selectedImageUri?.path != null) {
                val imageFile = selectedImageUri?.path?.let { File(it) }
                if (imageFile != null) {
                    val imagePart = prepareFilePart("image${LocalDateTime.now()}", imageFile)

                }
            }
        })
    }
}

fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    MusicAppTheme {
        EditProfileScreen() {}
    }
}