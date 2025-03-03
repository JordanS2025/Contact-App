package com.example.contact.models

data class PersonModel(
    val contactInfo: ContactModel,
    val pictureURL: String,
    val isFamilyMember: Boolean,
    val dateOfBirth: String // Additional property
) {
    override fun toString(): String {
        return "PersonModel(${super.toString()}, pictureURL='$pictureURL', isFamilyMember=$isFamilyMember, dateOfBirth='$dateOfBirth')"
    }
}