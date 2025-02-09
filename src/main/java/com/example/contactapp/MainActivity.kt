package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.contactapp.screens.ContactListScreen
import com.example.contactapp.ui.theme.ContactAppTheme

class MainActivity : ComponentActivity() {
    // Use lifecycle-aware ViewModel
    private val viewModel: ViewModelContacts by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContactAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactListScreen(viewModel)
                }
            }
        }
    }
}
