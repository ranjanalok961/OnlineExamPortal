package com.example.onlineexamportal

import android.content.Context
import android.content.SharedPreferences

object LoginInformation {

    private const val PREFS_NAME = "login_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ROLE = "user_role"

    private fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Save login information when a user successfully logs in
    fun saveLoginInfo(context: Context, role: String) {
        val editor = getSharedPrefs(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ROLE, role)
        editor.apply()
    }

    // Check if user is logged in
    fun isLoggedIn(context: Context): Boolean {
        return getSharedPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Retrieve the user role (e.g., Student or Professor)
    fun getUserRole(context: Context): String? {
        return getSharedPrefs(context).getString(KEY_USER_ROLE, null)
    }

    // Clear login information (for logout)
    fun clearLoginInfo(context: Context) {
        val editor = getSharedPrefs(context).edit()
        editor.clear()
        editor.apply()
    }
}
