package com.example.contactapp.screens

import android.content.Context
import android.widget.Toast
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
import com.example.contactapp.ViewModelContacts
import com.example.contactapp.models.ContactModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource

@Composable
fun ContactListScreen(viewModel: ViewModelContacts) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
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
                    // Section header
                    item {
                        Text(
                            text = initial.toString(),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Contact items
                    items(contacts) { contact ->
                        ContactItem(
                            contact = contact,
                            context = context,
                            onToggleExpand = { viewModel.toggleExpand(contact.id) }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Section spacing
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: ContactModel, context: Context, onToggleExpand: () -> Unit) {
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
            Text(
                text = contact.name,
                color = Color.White,
                fontSize = 18.sp
            )

            Button(
                onClick = onToggleExpand,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Details", color = Color.White)
            }
        }

        if (contact.isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("Phone: ${contact.phoneNumber}", color = Color.White)
                Text("Email: ${contact.email}", color = Color.White)
            }
        }
    }
}