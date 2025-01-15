package com.example.madcamp_wk3.utils

import android.content.Context
import android.content.SharedPreferences

object JwtUtils {
    private const val PREF_NAME = "LoginPrefs"
    private const val KEY_JWT_TOKEN = "jwt_token"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveJwtToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_JWT_TOKEN, token)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getJwtToken(context: Context): String? {
        return getPreferences(context).getString(KEY_JWT_TOKEN, null)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(KEY_JWT_TOKEN)
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.apply()
    }
}
