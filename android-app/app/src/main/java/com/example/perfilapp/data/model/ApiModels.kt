package com.example.perfilapp.data.model

data class RegisterRequest(val nombreCompleto: String, val correo: String, val password: String)
data class LoginRequest(val correo: String, val password: String)

data class UsuarioDto(val id: Int, val nombreCompleto: String, val correo: String)

data class RegisterResponse(val mensaje: String, val usuario: UsuarioDto)
data class AuthResponse(val token: String, val usuario: UsuarioDto)
data class ProfileResponse(val usuario: UsuarioDto)
data class ErrorResponse(val mensaje: String)
