package com.example.perfilapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfilapp.data.local.SessionManager
import com.example.perfilapp.data.model.UsuarioDto
import com.example.perfilapp.data.repository.ApiResult
import com.example.perfilapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val loading: Boolean = true,
    val usuario: UsuarioDto? = null,
    val error: String? = null,
    val photoUri: Uri? = null,       // SOLO local: nunca se envía al backend
    val sessionClosed: Boolean = false,
)

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AuthRepository(SessionManager(app))

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile() {
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            when (val r = repository.getProfile()) {
                is ApiResult.Success ->
                    _uiState.update { it.copy(loading = false, usuario = r.data.usuario) }
                is ApiResult.Error ->
                    if (r.httpCode == 401) logout()
                    else _uiState.update { it.copy(loading = false, error = r.message) }
            }
        }
    }

    fun setPhoto(uri: Uri) = _uiState.update { it.copy(photoUri = uri) }

    fun logout() {
        repository.logout()
        _uiState.update { it.copy(sessionClosed = true) }
    }
}
