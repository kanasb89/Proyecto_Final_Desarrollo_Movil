package com.example.perfilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.perfilapp.data.local.SessionManager
import com.example.perfilapp.ui.LoginScreen
import com.example.perfilapp.ui.ProfileScreen
import com.example.perfilapp.ui.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startDestination =
            if (SessionManager(this).getToken() != null) "profile" else "login"

        setContent {
            MaterialTheme {
                Surface { AppNavigation(startDestination) }
            }
        }
    }
}

@Composable
private fun AppNavigation(startDestination: String) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    nav.navigate("profile") { popUpTo("login") { inclusive = true } }
                },
                onGoToRegister = { nav.navigate("register") },
            )
        }
        composable("register") {
            RegisterScreen(
                onRegistered = { nav.popBackStack() },
                onBack = { nav.popBackStack() },
            )
        }
        composable("profile") {
            ProfileScreen(
                onLoggedOut = {
                    nav.navigate("login") { popUpTo(0) }
                },
            )
        }
    }
}
