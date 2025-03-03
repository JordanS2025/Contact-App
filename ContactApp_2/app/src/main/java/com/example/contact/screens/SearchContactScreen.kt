package com.example.contact.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contact.Screen
import com.example.contact.ViewModelContacts
import com.example.contact.models.ContactModel

@Composable
fun SearchContactScreen(viewModel: ViewModelContacts, modifier: Modifier = Modifier, navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    // Track if search has been performed
    var hasSearched by remember { mutableStateOf(false) }

    // Use the filteredContacts for results
    val searchResults = remember { viewModel.filteredContacts }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Search Contacts", style = MaterialTheme.typography.headlineSmall)

        // Search Input
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter search criteria") },
            modifier = Modifier.fillMaxWidth()
        )

        // Search Button
        Button(
            onClick = {
                viewModel.findByName(searchText.text)
                hasSearched = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        // Results section
        if (hasSearched) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (searchResults.isEmpty()) "No results found" else "Results (${searchResults.size})",
                style = MaterialTheme.typography.titleMedium
            )

            // Display search results
            LazyColumn {
                items(searchResults) { contact ->
                    ContactItem(contact, onClick = {
                        navController.navigate("contactDetail/${contact.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: ContactModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
            Text(text = contact.phoneNumber, style = MaterialTheme.typography.bodyMedium)
            Text(text = contact.email, style = MaterialTheme.typography.bodyMedium)
        }
    }
}