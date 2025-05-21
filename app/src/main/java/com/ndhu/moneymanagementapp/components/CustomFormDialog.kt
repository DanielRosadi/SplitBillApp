package com.ndhu.moneymanagementapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
    val visualTransformation: VisualTransformation = VisualTransformation.None // e.g., password masking
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFormDialog(
    showDialog: MutableState<Boolean>,      // Controls dialog visibility
    title: String,                          // Dialog title
    icon: ImageVector? = null,              // Optional icon at the top
    fields: List<FormField>,                // List of input fields to display
    errorMessage: String? = null,           // Optional error message
    confirmButtonText: String = "Confirm",  // Text for confirm button
    cancelButtonText: String = "Cancel",    // Text for cancel button
    onConfirm: () -> Unit,                  // Action when confirm is clicked
    onCancel: () -> Unit                    // Action when cancel is clicked
) {
    val context = LocalContext.current

    // Main dialog
    AlertDialog(
        onDismissRequest = { showDialog.value = false }, // Dismiss when clicking outside
        containerColor = Color(0xFFF5F5F5), // White Smoke
        shape = RoundedCornerShape(20.dp),

        // Title section with optional icon
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display icon if provided
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
                fields.forEach { field ->
                    OutlinedTextField(
                        value = field.value,
                        onValueChange = field.onValueChange,
                        label = {
                            Text(
                                text = field.label,
                                color = primaryGreen,
                                fontSize = 14.sp
                            )
                        },
                        keyboardOptions = field.keyboardOptions,
                        visualTransformation = field.visualTransformation,
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
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
                }

                // Display error message if present
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

        // Confirm and cancel buttons
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel button
                OutlinedButton(
                    onClick = {
                        onCancel()
                        showDialog.value = false
                    },
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

                // Confirm button
                Button(
                    onClick = {
                        onConfirm()
                        showDialog.value = false
                    },
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