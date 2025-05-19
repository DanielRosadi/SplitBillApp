package com.ndhu.moneymanagementapp.utils

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.ui.input.key.Key
import androidx.datastore.preferences.core.stringPreferencesKey
import at.favre.lib.crypto.bcrypt.BCrypt
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.InputStream

object Util {

    private const val PREF_NAME = "UserPreferences"
    private const val KEY_USER_ID = "user_id"

    fun identifyBy(identify : String) : String {
        if(identify.contains("@")) {
            return "query by email"
        }
        return "query by username"
    }

    fun passwordEncryption(password : String) : String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password : String, hashedPassword : String) : Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
    fun saveUserId(context: Context, userId: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun clearUserId(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(KEY_USER_ID).apply()
    }

    fun processImage(scannedText : String, photoBitmap: Bitmap) : String {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val image = photoBitmap.let {
            InputImage.fromBitmap(it, 0)
        }

        var result = ""

        image.let {
            recognizer.process(it).addOnSuccessListener { scannedText ->
                result = scannedText.text
            }.addOnFailureListener { exception ->
                Log.e("KEY", exception.toString())
            }
        }
        return result
    }

    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    fun Context.shareText(message: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }.also { intent ->
            startActivity(intent)
        }
    }

    fun Context.copyToClipboard(content: String) {
        val clipboardManager = ContextCompat.getSystemService(this, ClipboardManager::class.java)!!
        val clip = ClipData.newPlainText("clipboard", content)
        clipboardManager.setPrimaryClip(clip)
    }

    fun Context.vibrate(duration: Long = 50) {
        val vibrator = getSystemService(Vibrator::class.java)
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        duration, VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        }
    }

    fun View.onVibrationClick(action: () -> Unit) {
        setOnClickListener {
            this.context.vibrate()
            action()
        }
    }

    fun Uri.toBitmap(context: Context): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(this)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    // to validate user existence
    fun isUserLoggedIn(context: Context): Boolean {
        val userController = com.ndhu.moneymanagementapp.controllers.UserController()
        return kotlinx.coroutines.runBlocking {
            userController.getOnboardUser(context) != null
        }
    }

}