package com.example.contact.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contact.ViewModelContacts
import com.example.contact.models.*

@Composable
fun AddContactScreen(viewModel: ViewModelContacts, modifier: Modifier = Modifier, navController: NavController) {
    // Your state variables
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var street by remember { mutableStateOf(TextFieldValue("")) }
    var city by remember { mutableStateOf(TextFieldValue("")) }
    var state by remember { mutableStateOf(TextFieldValue("")) }
    var postalCode by remember { mutableStateOf(TextFieldValue("")) }

    var isBusiness by remember { mutableStateOf(false) }
    var webUrl by remember { mutableStateOf(TextFieldValue("")) }
    var myOpinionRating by remember { mutableStateOf(TextFieldValue("")) }
    var isFamilyMember by remember { mutableStateOf(false) }
    var dateOfBirth by remember { mutableStateOf(TextFieldValue("")) }
    var daysOpen by remember { mutableStateOf(BooleanArray(7)) }

    // Remember scroll state to enable scrolling
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            // Add vertical scrolling capability to the column
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Add New Contact", style = MaterialTheme.typography.headlineSmall)

        // Contact Details
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Street Address") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Postal Code") }, modifier = Modifier.fillMaxWidth())

        // Contact Type Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Personal")
            Switch(checked = isBusiness, onCheckedChange = { isBusiness = it })
            Text(text = "Business")
        }

        // Business-Specific Fields
        if (isBusiness) {
            OutlinedTextField(value = webUrl, onValueChange = { webUrl = it }, label = { Text("Web URL") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = myOpinionRating, onValueChange = { myOpinionRating = it }, label = { Text("Opinion Rating (1-5)") }, modifier = Modifier.fillMaxWidth())

            // Days Open Checkboxes
            Text(text = "Days Open")
            val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            Column {
                days.forEachIndexed { index, day ->
                    Row {
                        Checkbox(
                            checked = daysOpen[index],
                            onCheckedChange = { checked ->
                                daysOpen = daysOpen.toMutableList().apply { this[index] = checked }.toBooleanArray()
                            }
                        )
                        Text(text = day)
                    }
                }
            }
        } else {
            // Personal-Specific Fields
            OutlinedTextField(value = dateOfBirth, onValueChange = { dateOfBirth = it }, label = { Text("Date of Birth") }, modifier = Modifier.fillMaxWidth())

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = isFamilyMember, onCheckedChange = { isFamilyMember = it })
                Text(text = "Is Family Member")
            }
        }

        // Add some space before buttons
        Spacer(modifier = Modifier.height(16.dp))

        // OK and Cancel Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    // Create ContactModel
                    val baseContact = ContactModel(
                        id = 0, // Temporary ID (Assuming ViewModel handles assigning unique IDs)
                        name = name.text,
                        email = email.text,
                        phoneNumber = phone.text,
                        addressStreet = street.text,
                        addressCity = city.text,
                        addressState = state.text,
                        addressPostalCode = postalCode.text
                    )

                    // Create Business or Personal Contact
                    val contact = if (isBusiness) {
                        BusinessModel(
                            contactInfo = baseContact,
                            webURL = webUrl.text,
                            myOpinionRating = myOpinionRating.text.toIntOrNull() ?: 0,
                            daysOpen = daysOpen.toList(),
                            businessType = "General" // You can modify this to add an input for business type
                        )
                    } else {
                        PersonModel(
                            contactInfo = baseContact,
                            pictureURL = "", // Can be updated to include an image picker later
                            isFamilyMember = isFamilyMember,
                            dateOfBirth = dateOfBirth.text
                        )
                    }

                    viewModel.addOne(contact)
                    navController.popBackStack() // Navigate back
                }
            ) {
                Text("OK")
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel")
            }
        }

        // Add bottom padding to ensure the buttons are not cut off
        Spacer(modifier = Modifier.height(24.dp))
    }
}