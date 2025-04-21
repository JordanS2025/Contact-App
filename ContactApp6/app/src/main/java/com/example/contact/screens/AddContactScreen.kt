package com.example.contact.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contact.data.ContactViewModelJSON
import com.example.contact.models.*

@Composable
fun AddContactScreen(viewModel: ContactViewModelJSON, modifier: Modifier = Modifier, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    var isBusiness by remember { mutableStateOf(false) }
    var webUrl by remember { mutableStateOf("") }
    var myOpinionRating by remember { mutableStateOf("") }
    var isFamilyMember by remember { mutableStateOf(false) }
    var dateOfBirth by remember { mutableStateOf("") }
    var daysOpen by remember { mutableStateOf(BooleanArray(7) { false }) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Add New Contact", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Street Address") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Postal Code") }, modifier = Modifier.fillMaxWidth())

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Personal")
            Switch(checked = isBusiness, onCheckedChange = { isBusiness = it })
            Text(text = "Business")
        }

        if (isBusiness) {
            OutlinedTextField(value = webUrl, onValueChange = { webUrl = it }, label = { Text("Web URL") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = myOpinionRating, onValueChange = { myOpinionRating = it }, label = { Text("Opinion Rating (1-5)") }, modifier = Modifier.fillMaxWidth())

            Text(text = "Days Open")
            val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            Column {
                days.forEachIndexed { index, day ->
                    Row {
                        Checkbox(
                            checked = daysOpen[index],
                            onCheckedChange = { checked ->
                                daysOpen = daysOpen.copyOf().apply { this[index] = checked }
                            }
                        )
                        Text(text = day)
                    }
                }
            }
        } else {
            OutlinedTextField(value = dateOfBirth, onValueChange = { dateOfBirth = it }, label = { Text("Date of Birth") }, modifier = Modifier.fillMaxWidth())
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = isFamilyMember, onCheckedChange = { isFamilyMember = it })
                Text(text = "Is Family Member")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val contact = if (isBusiness) {
                        BusinessModel(
                            id = 0, // Will be set by ViewModel
                            name = name,
                            email = email,
                            phoneNumber = phone,
                            addressStreet = street,
                            addressCity = city,
                            addressState = state,
                            addressPostalCode = postalCode,
                            webURL = webUrl,
                            myOpinionRating = myOpinionRating.toIntOrNull() ?: 0,
                            daysOpen = daysOpen.toList(),
                            businessType = "General"
                        )
                    } else {
                        PersonModel(
                            id = 0, // Will be set by ViewModel
                            name = name,
                            email = email,
                            phoneNumber = phone,
                            addressStreet = street,
                            addressCity = city,
                            addressState = state,
                            addressPostalCode = postalCode,
                            pictureURL = "",
                            isFamilyMember = isFamilyMember,
                            dateOfBirth = dateOfBirth
                        )
                    }
                    viewModel.addContact(contact)
                    navController.popBackStack()
                }
            ) {
                Text("OK")
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel")
            }
        }
    }
}