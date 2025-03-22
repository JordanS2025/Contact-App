package com.example.contact.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contact.data.ContactViewModelInterface
import com.example.contact.models.ContactModel

@Composable
fun SearchContactScreen(
    viewModel: ContactViewModelInterface,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    // State for the search query
    var searchText by remember { mutableStateOf("") }

    // Filter contacts based on the search query
    val filteredContacts = viewModel.contactsList.filter {
        it.name.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = modifier
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

        // Results section
        Spacer(modifier = Modifier.height(16.dp))
        if (searchText.isNotEmpty()) {
            Text(
                text = if (filteredContacts.isEmpty()) "No results found" else "Results (${filteredContacts.size})",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn {
                items(filteredContacts) { contact ->
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