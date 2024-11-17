package com.fpoly.pro226.music_app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fpoly.pro226.music_app.ui.theme._00C2CB

@Composable
fun LoadingDialog(onDismiss: () -> Unit) {
    AlertDialog(
        modifier = Modifier.size(100.dp),
        onDismissRequest = onDismiss,
        title = null,
        buttons = {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = _00C2CB)
            }
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