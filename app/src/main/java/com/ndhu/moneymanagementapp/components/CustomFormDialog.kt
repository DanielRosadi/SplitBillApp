package com.ndhu.moneymanagementapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ndhu.moneymanagementapp.ui.pages.primaryGreen

// Data class to represent a form field
data class FormField(
    val label: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val visualTransformation: VisualTransformation = VisualTransformation.None, // e.g., password masking
    val error: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFormDialog(
    showDialog: MutableState<Boolean>,       // Controls dialog visibility
    title: String,                           // Dialog title
    icon: ImageVector? = null,               // Optional top icon
    fields: List<FormField>,                 // List of form input fields
    errorMessage: String? = null,            // Optional general error message
    confirmButtonText: String = "Confirm",   // Confirm button label
    cancelButtonText: String = "Cancel",     // Cancel button label
    onConfirm: () -> Unit,                   // Logic when confirm is clicked
    onCancel: () -> Unit,                    // Logic when cancel is clicked
    confirmButtonEnabled: Boolean = true     // Whether confirm button is enabled
) {
    val context = LocalContext.current

    // Map to track password visibility toggles for each field
    val passwordVisibilityStates = remember { mutableStateMapOf<Int, Boolean>() }

    // Dialog container
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        containerColor = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(20.dp),

        // Header section with optional icon and title
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(bottom = 4.dp)
                    )
                }
                // Title text
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },

        // Main content area with form fields and error message
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Render each form field
                fields.forEachIndexed { index, field ->
                    val isPasswordField = field.visualTransformation is PasswordVisualTransformation
                    val isVisible = passwordVisibilityStates.getOrPut(index) { false }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Input field
                        OutlinedTextField(
                            value = field.value,
                            onValueChange = field.onValueChange,
                            isError = field.error != null,
                            label = {
                                Text(
                                    text = field.label,
                                    color = primaryGreen,
                                    fontSize = 14.sp
                                )
                            },
                            keyboardOptions = field.keyboardOptions,
                            visualTransformation = if (isPasswordField && !isVisible)
                                PasswordVisualTransformation() else VisualTransformation.None,
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            trailingIcon = {
                                if (isPasswordField) {
                                    IconButton(onClick = {
                                        passwordVisibilityStates[index] = !isVisible
                                    }) {
                                        Icon(
                                            imageVector = if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = if (isVisible) "Hide password" else "Show password",
                                            tint = primaryGreen
                                        )
                                    }
                                }
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = primaryGreen,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = primaryGreen
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        )

                        // Inline field-specific error message
                        if (field.error != null) {
                            Text(
                                text = field.error,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }
                    }
                }

                // General error message (if any)
                if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }
        },

        // Confirm + Cancel buttons
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel button
                OutlinedButton(
                    onClick = { onCancel() },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF666666) // cancelGray
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                ) {
                    Text(cancelButtonText, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Confirm button (conditionally enabled)
                Button(
                    onClick = { onConfirm() },
                    enabled = confirmButtonEnabled, // Button disables if form is invalid
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text(confirmButtonText)
                }
            }
        }
    )
}