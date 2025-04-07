package com.example.contact.models

data class PersonModel(
    override val id: Int,
    override val name: String,
    override val email: String,
    override val phoneNumber: String,
    override val addressStreet: String,
    override val addressCity: String,
    override val addressState: String,
    override val addressPostalCode: String,
    val pictureURL: String,
    val isFamilyMember: Boolean,
    val dateOfBirth: String
) : ContactModel(id, name, email, phoneNumber, addressStreet, addressCity, addressState, addressPostalCode)