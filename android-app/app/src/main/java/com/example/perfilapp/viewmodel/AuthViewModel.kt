package com.example.perfilapp.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfilapp.data.local.SessionManager
import com.example.perfilapp.data.repository.ApiResult
import com.example.perfilapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val loggedIn: Boolean = false,
    val registered: Boolean = false,
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AuthRepository(SessionManager(app))

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(correo: String, password: String) {
        validateEmail(correo)?.let { return setError(it) }
        if (password.isBlank()) return setError("La contraseña es obligatoria")

        launchRequest {
            when (val r = repository.login(correo.trim(), password)) {
                is ApiResult.Success -> _uiState.update { it.copy(loading = false, loggedIn = true) }
                is ApiResult.Error -> setError(r.message)
            }
        }
    }

    fun register(nombre: String, correo: String, password: String) {
        if (nombre.isBlank()) return setError("El nombre es obligatorio")
        validateEmail(correo)?.let { return setError(it) }
        if (password.length < 6) return setError("La contraseña debe tener al menos 6 caracteres")

        launchRequest {
            when (val r = repository.register(nombre.trim(), correo.trim(), password)) {
                is ApiResult.Success -> _uiState.update { it.copy(loading = false, registered = true) }
                is ApiResult.Error -> setError(r.message)
            }
        }
    }

    private fun validateEmail(correo: String): String? = when {
        correo.isBlank() -> "El correo es obligatorio"
        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> "Formato de correo inválido"
        else -> null
    }

    private fun launchRequest(block: suspend () -> Unit) {
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch { block() }
    }

    private fun setError(message: String) = _uiState.update { it.copy(loading = false, error = message) }
}
