package com.example.contactapp.models

data class ContactModel(
    val id: Int,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val addressStreet: String,
    val addressCity: String,
    val addressState: String,
    val addressPostalCode: String,
    var isExpanded: Boolean = false
) {
    override fun toString(): String {
        return "ContactModel(id=$id, name='$name', email='$email', phoneNumber='$phoneNumber', address='$addressStreet, $addressCity, $addressState, $addressPostalCode', isExpanded=$isExpanded)"
    }
}