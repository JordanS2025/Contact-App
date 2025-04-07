package com.example.contact.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.contact.models.BusinessModel
import com.example.contact.models.ContactModel
import com.example.contact.models.PersonModel

class ViewModelInMemory : ContactViewModelInterface, ViewModel() {
    override var contactsList: MutableList<ContactModel> = mutableStateListOf()

    override fun loadContactsFromJSON() {
        // No-op for in-memory version
    }

    override fun saveContactsToJSON() {
        // No-op for in-memory version
    }

    override fun addContact(contact: ContactModel) {
        val newId = contactsList.size + 1
        val newContact = when (contact) {
            is BusinessModel -> contact.copy(id = newId)
            is PersonModel -> contact.copy(id = newId)
            else -> throw IllegalArgumentException("Unsupported ContactModel type: ${contact::class.simpleName}")
        }
        contactsList.add(newContact)
    }

    override fun removeContact(contact: ContactModel) {
        contactsList.remove(contact)
    }
}