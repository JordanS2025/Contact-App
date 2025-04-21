// In ContactListScreen.kt
package com.example.contact.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.contact.data.ContactViewModelJSON
import com.example.contact.models.*

@Composable
fun ContactListScreen(
    viewModel: ContactViewModelJSON,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Contact List",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (viewModel.contactsList.isEmpty()) {
            Text("No contacts available.", color = Color.Gray)
        } else {
            val groupedContacts = viewModel.contactsList
                .sortedBy { it.name }
                .groupBy { it.name.first().uppercaseChar() }
                .toSortedMap()

            LazyColumn {
                groupedContacts.forEach { (initial, contacts) ->
                    item {
                        Text(
                            text = initial.toString(),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(contacts) { contact ->
                        ContactItem(
                            contact = contact,
                            context = context,
                            isExpanded = expandedStates[contact.id] ?: false,
                            onToggleExpand = {
                                expandedStates[contact.id] = !(expandedStates[contact.id] ?: false)
                            },
                            onClick = { navController.navigate("contact_detail/${contact.id}") }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

// In ContactListScreen.kt
@Composable
fun ContactItem(
    contact: ContactModel,
    context: Context,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.clickable { onClick() }) {
                Text(
                    text = contact.name,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            Button(
                onClick = onToggleExpand,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(if (isExpanded) "Hide" else "Details", color = Color.White)
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("Phone: ${contact.phoneNumber}", color = Color.White)
                Text("Email: ${contact.email}", color = Color.White)
                when (contact) {
                    is BusinessModel -> {
                        Text("Web URL: ${contact.webURL}", color = Color.White)
                        Text("Rating: ${contact.myOpinionRating}", color = Color.White)
                        Text(
                            "Days Open: ${
                                contact.daysOpen.mapIndexed { i, open ->
                                    if (open) listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")[i] else null
                                }.filterNotNull().joinToString(", ")
                            }",
                            color = Color.White
                        )
                    }
                    is PersonModel -> {
                        Text("Family Member: ${if (contact.isFamilyMember) "Yes" else "No"}", color = Color.White)
                        Text("Date of Birth: ${contact.dateOfBirth}", color = Color.White)
                    }
                }
            }
        }
    }
}