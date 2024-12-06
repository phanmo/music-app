package com.fpoly.pro226.music_app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fpoly.pro226.music_app.R

@Composable
fun LoadingDialog(onDismiss: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading33))
    val progressLottie by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 7f
    )
    AlertDialog(
        modifier = Modifier.size(150.dp),
        onDismissRequest = onDismiss,
        title = null,
        contentColor = Color.Transparent,
        backgroundColor = Color.Transparent,
        buttons = {
            LottieAnimation(
                composition, progressLottie,
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun LoadingDialogPreview() {
    MaterialTheme {
        LoadingDialog { }
    }
}