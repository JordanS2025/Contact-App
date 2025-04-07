package com.example.contact.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contact.data.ContactViewModelJSON
import com.example.contact.models.*

@Composable
fun ContactDetailScreen(
    viewModel: ContactViewModelJSON,
    contactId: Int,
    modifier: Modifier = Modifier
) {
    val contact = viewModel.contactsList.find { it.id == contactId }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        if (contact != null) {
            // Contact Name
            Text(
                text = contact.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Phone Section
            if (contact.phoneNumber.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Phone: ${contact.phoneNumber}", color = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phoneNumber}"))
                        context.startActivity(intent)
                    }) {
                        Text("Call")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${contact.phoneNumber}"))
                        context.startActivity(intent)
                    }) {
                        Text("Text")
                    }
                }
            } else {
                Text("No phone number", color = Color.Gray)
            }
            Spacer(Modifier.height(8.dp))

            // Address Section
            if (contact.addressStreet.isNotEmpty() && contact.addressCity.isNotEmpty() &&
                contact.addressState.isNotEmpty() && contact.addressPostalCode.isNotEmpty()
            ) {
                val address = "${contact.addressStreet}, ${contact.addressCity}, ${contact.addressState} ${contact.addressPostalCode}"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Address: $address", color = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(address)}"))
                        context.startActivity(intent)
                    }) {
                        Text("Map")
                    }
                }
            } else {
                Text("No address", color = Color.Gray)
            }
            Spacer(Modifier.height(8.dp))

            // Email Section
            if (contact.email.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Email: ${contact.email}", color = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${contact.email}"))
                        context.startActivity(intent)
                    }) {
                        Text("Email")
                    }
                }
            } else {
                Text("No email", color = Color.Gray)
            }
        } else {
            Text("Contact not found", color = Color.Red)
        }
    }
}