package com.example.contact

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.contact.data.ContactViewModelJSON
import com.example.contact.screens.AddContactScreen
import com.example.contact.screens.ContactDetailScreen
import com.example.contact.screens.ContactListScreen
import com.example.contact.screens.SearchContactScreen
import com.example.contact.ui.theme.ContactTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ContactViewModelJSON by viewModels {
        ContactViewModelJSONFactory(this)
    }

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

class ContactViewModelJSONFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModelJSON::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactViewModelJSON(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class Screen(val route: String) {
    object ContactList : Screen("contact_list")
    object AddContact : Screen("add_contact")
    object SearchContact : Screen("search_contact")
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(Screen.ContactList, Screen.AddContact, Screen.SearchContact)
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.route.replace("_", " ").capitalize()) },
                selected = false,
                onClick = { navController.navigate(screen.route) },
                icon = {}
            )
        }
    }
}

@Composable
fun ContactApp(viewModel: ContactViewModelJSON) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ContactList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ContactList.route) {
                ContactListScreen(viewModel, navController, Modifier.fillMaxSize())
            }
            composable(Screen.AddContact.route) {
                AddContactScreen(viewModel, Modifier.fillMaxSize(), navController)
            }
            composable(Screen.SearchContact.route) {
                SearchContactScreen(viewModel, Modifier.fillMaxSize(), navController)
            }
            composable(
                "contact_detail/{contactId}",
                arguments = listOf(navArgument("contactId") { type = NavType.IntType })
            ) { backStackEntry ->
                val contactId = backStackEntry.arguments?.getInt("contactId") ?: 0
                ContactDetailScreen(viewModel, contactId, Modifier.fillMaxSize())
            }
        }
    }
}