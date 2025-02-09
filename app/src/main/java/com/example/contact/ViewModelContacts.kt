package com.example.contact

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.contact.models.ContactModel

class ViewModelContacts : ViewModel() {

    private var _contactsList = mutableStateListOf<ContactModel>()
    private var _contactsSearchResults = mutableStateListOf<ContactModel>()

    val contactsList: List<ContactModel>
        get() = _contactsList

    val contactsSearchResults: List<ContactModel>
        get() = _contactsSearchResults

    fun addOne(contact: ContactModel) {
        _contactsList.add(contact)
    }

    fun deleteOne(id: Int): Int? {
        val contact = _contactsList.find { it.id == id }
        return if (contact != null && _contactsList.remove(contact)) {
            id
        } else {
            null
        }
    }

    fun getOne(id: Int): ContactModel? {
        return _contactsList.find { it.id == id }
    }

    fun searchByName(name: String): List<ContactModel> {
        return _contactsList.filter { it.name.contains(name, ignoreCase = true) }
    }

    fun updateOne(contact: ContactModel): ContactModel? {
        val index = _contactsList.indexOfFirst { it.id == contact.id }
        return if (index != -1) {
            _contactsList[index] = contact
            contact
        } else {
            null
        }
    }

    fun saveAll() { /* To be implemented later */ }
    fun loadAll() { /* To be implemented later */ }

    //
    fun toggleExpand(contactId: Int) {
        val index = _contactsList.indexOfFirst { it.id == contactId }
        if (index != -1) {
            val updated = _contactsList[index].copy(
                isExpanded = !_contactsList[index].isExpanded
            )
            _contactsList[index] = updated
        }
    }

    init {
        // Hardcoded Contacts
        _contactsList.addAll(
            listOf(
                ContactModel(1, "John Doe", "john@example.com", "123-456-7890", "123 Main St", "City A", "State A", "12345"),
                ContactModel(2, "Jane Smith", "jane@example.com", "987-654-3210", "456 Elm St", "City B", "State B", "67890"),
                ContactModel(3, "Michael Brown", "michael@example.com", "555-123-4567", "789 Oak St", "City C", "State C", "11223"),
                ContactModel(4, "Emily Davis", "emily@example.com", "222-333-4444", "321 Pine St", "City D", "State D", "33445"),
                ContactModel(5, "David Wilson", "david@example.com", "111-222-3333", "654 Maple St", "City E", "State E", "55667")
            )
        )
    }

}
