package com.ndhu.moneymanagementapp.sharedviewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// not used for now
class AssignSplitsSnackBarViewModel : ViewModel() {
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun snackbarShown() {
        _snackbarMessage.value = null
    }
}