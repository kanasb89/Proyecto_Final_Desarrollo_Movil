package com.example.perfilapp.data.repository

import com.example.perfilapp.data.local.SessionManager
import com.example.perfilapp.data.model.*
import com.example.perfilapp.data.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val httpCode: Int? = null) : ApiResult<Nothing>()
}

class AuthRepository(private val session: SessionManager) {

    private val api = RetrofitClient.api

    suspend fun register(nombre: String, correo: String, password: String) =
        safeCall { api.register(RegisterRequest(nombre, correo, password)) }

    suspend fun login(correo: String, password: String): ApiResult<AuthResponse> {
        val result = safeCall { api.login(LoginRequest(correo, password)) }
        if (result is ApiResult.Success) session.saveToken(result.data.token)
        return result
    }

    suspend fun getProfile(): ApiResult<ProfileResponse> {
        val token = session.getToken() ?: return ApiResult.Error("Sesión no iniciada", 401)
        return safeCall { api.getMe("Bearer $token") }
    }

    fun hasSession() = session.getToken() != null
    fun logout() = session.clear()

    private suspend fun <T> safeCall(call: suspend () -> Response<T>): ApiResult<T> = try {
        val response = call()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            ApiResult.Error(parseError(response.errorBody()?.string()), response.code())
        }
    } catch (e: IOException) {
        ApiResult.Error("No se pudo conectar con el servidor. Revisa tu conexión.")
    } catch (e: Exception) {
        ApiResult.Error("Ocurrió un error inesperado.")
    }

    private fun parseError(json: String?): String = try {
        Gson().fromJson(json, ErrorResponse::class.java).mensaje
    } catch (e: Exception) {
        "Error del servidor"
    }
}
