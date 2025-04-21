package com.example.contact.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contact.data.ContactViewModelInterface
import com.example.contact.models.ContactModel
import com.example.contact.models.PersonModel

@Composable
fun SearchContactScreen(
    viewModel: ContactViewModelInterface,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }

    // Filter states
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var showFamilyOnly by remember { mutableStateOf(false) }
    var sortByRecent by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }

    // All groups (you can replace this with a dynamic list from ViewModel if needed)
    val availableGroups = listOf("Work", "Friends", "Gym", "School")

    // Apply filters
    val filteredContacts = viewModel.contactsList
        .filter { it.name.contains(searchText, ignoreCase = true) }
        .filter {
            (!showFavoritesOnly || it.isFavorite == true) &&
                    (!showFamilyOnly || (it is PersonModel && it.isFamilyMember)) &&
                    (selectedGroup == null || it.groups?.contains(selectedGroup) == true)
        }
        .let { list ->
            if (sortByRecent) list.sortedByDescending { it.lastUsed ?: 0L }
            else list
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

        // Filter Toggle Button
        TextButton(onClick = { showFilters = !showFilters }) {
            Text(if (showFilters) "Hide Filters" else "Show Filters")
        }

        // Filter Options
        if (showFilters) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = showFavoritesOnly, onCheckedChange = { showFavoritesOnly = it })
                    Text("Show Favorites Only")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = showFamilyOnly, onCheckedChange = { showFamilyOnly = it })
                    Text("Show Family Members Only")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = sortByRecent, onCheckedChange = { sortByRecent = it })
                    Text("Sort by Most Recently Used")
                }
                // Dropdown for groups
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text("Group: ${selectedGroup ?: "All"}")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(onClick = {
                            selectedGroup = null
                            expanded = false
                        }, text = { Text("All") })

                        availableGroups.forEach { group ->
                            DropdownMenuItem(onClick = {
                                selectedGroup = group
                                expanded = false
                            }, text = { Text(group) })
                        }
                    }
                }
            }
        }

        // Results section
        Spacer(modifier = Modifier.height(16.dp))
        if (searchText.isNotEmpty() || showFilters) {
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