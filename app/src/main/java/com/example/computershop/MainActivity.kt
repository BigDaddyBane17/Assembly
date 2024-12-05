package com.example.computershop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.navigation.NavigationHost
import com.example.computershop.presentation.client_screens.AuthenticationViewModel
import com.example.computershop.ui.theme.ComputerShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val db = DatabaseHelper(this)
        val authenticationViewModel = AuthenticationViewModel(db)
        enableEdgeToEdge()
        setContent {
            ComputerShopTheme {
                db.insertManager("manager1", "123")
                db.insertManager("manager2", "123")
                NavigationHost(databaseHelper = db, authenticationViewModel = authenticationViewModel)
            }
        }
    }
}
