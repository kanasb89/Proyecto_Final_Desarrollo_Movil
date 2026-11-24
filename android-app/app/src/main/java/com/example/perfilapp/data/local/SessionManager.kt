package com.example.perfilapp.data.local

import android.content.Context

/** Guarda solo el token JWT (texto). La foto de perfil NO se guarda aquí. */
class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveToken(token: String) = prefs.edit().putString(KEY_TOKEN, token).apply()
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun clear() = prefs.edit().clear().apply()

    private companion object { const val KEY_TOKEN = "token" }
}
