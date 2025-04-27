package com.ndhu.moneymanagementapp.utils

import androidx.compose.ui.input.key.Key
import androidx.datastore.preferences.core.stringPreferencesKey
import at.favre.lib.crypto.bcrypt.BCrypt
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

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

}