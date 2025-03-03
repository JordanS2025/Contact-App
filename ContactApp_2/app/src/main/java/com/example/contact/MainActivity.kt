package com.example.contact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.contact.screens.ContactListScreen
import com.example.contact.screens.AddContactScreen
import com.example.contact.screens.SearchContactScreen
import com.example.contact.ui.theme.ContactTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ViewModelContacts by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContactTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactApp(viewModel)
                }
            }
        }
    }
}

// Define navigation routes
sealed class Screen(val route: String) {
    object ContactList : Screen("contact_list")
    object AddContact : Screen("add_contact")
    object SearchContact : Screen("search_contact")
}

// Bottom Navigation Bar
@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.ContactList,
        Screen.AddContact,
        Screen.SearchContact
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.route.replace("_", " ").capitalize()) },
                selected = false, // Handle selection state properly if needed
                onClick = { navController.navigate(screen.route) },
                icon = {} // You can add icons here if needed
            )
        }
    }
}

// Main App with Navigation
@Composable
fun ContactApp(viewModel: ViewModelContacts) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding -> // Pass innerPadding to avoid overlapping UI
        NavHost(
            navController = navController,
            startDestination = Screen.ContactList.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.ContactList.route) {
                ContactListScreen(viewModel, Modifier.padding(innerPadding))
            }
            composable(Screen.AddContact.route) { navBackStackEntry ->
                AddContactScreen(viewModel, navController = navController) // Pass navController
            }
            composable(Screen.SearchContact.route) {
                SearchContactScreen(viewModel, Modifier.padding(innerPadding), navController)
            }
        }
    }
}

