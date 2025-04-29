package com.ndhu.moneymanagementapp.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ndhu.moneymanagementapp.utils.Util.toBitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
@Composable
fun OcrScreen() {
    var scannedText by remember { mutableStateOf<String?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val recognizer = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = uri.toBitmap(context)
            selectedImageBitmap = bitmap

            bitmap?.let { bmp ->
                val inputImage = InputImage.fromBitmap(bmp, 0)
                recognizer.process(inputImage)
                    .addOnSuccessListener { result ->
                        scannedText = result.text
                    }
                    .addOnFailureListener { e ->
                        scannedText = "Failed to recognize text: ${e.localizedMessage}"
                    }
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Scan Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedImageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = scannedText ?: "No text scanned yet.")
    }
}
