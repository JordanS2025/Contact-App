package com.example.contact.data

import com.example.contact.models.ContactModel

interface ContactViewModelInterface {
    var contactsList: MutableList<ContactModel> // Base type can hold BusinessModel or PersonModel

    fun loadContactsFromJSON()
    fun saveContactsToJSON()
    fun addContact(contact: ContactModel)
    fun removeContact(contact: ContactModel)
}