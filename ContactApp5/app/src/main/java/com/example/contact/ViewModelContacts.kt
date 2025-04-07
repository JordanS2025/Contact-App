package com.example.contact

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.contact.models.BusinessModel
import com.example.contact.models.ContactModel
import com.example.contact.models.PersonModel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

class ViewModelContacts(private val context: Context) : ViewModel() {
    private val file = File(context.filesDir, "contacts.json")
    private val mapper = jacksonObjectMapper()

    private var _contactsList = mutableStateListOf<ContactModel>()
    val filteredContacts = mutableStateListOf<ContactModel>() // Store search results

    val contactsList: List<ContactModel>
        get() = _contactsList

    init {
        loadAll()
    }

    fun addOne(contact: Any) {
        val newId = (_contactsList.maxOfOrNull { it.id } ?: 0) + 1
        when (contact) {
            is BusinessModel -> {
                val newContact = contact.copy(id = newId)
                _contactsList.add(newContact)
            }
            is PersonModel -> {
                val newContact = contact.copy(id = newId)
                _contactsList.add(newContact)
            }
            is ContactModel -> {
                val newContact = contact // Shouldn't happen with proper type handling
                _contactsList.add(newContact)
            }
        }
        saveAll()
    }

    fun deleteOne(id: Int): Int? {
        val contact = _contactsList.find { it.id == id }
        return if (contact != null && _contactsList.remove(contact)) {
            saveAll()
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
            saveAll()
            contact
        } else {
            null
        }
    }

    fun saveAll() {
        Log.d("ViewModelContacts", "Saving contacts to JSON")
        try {
            mapper.writeValue(file, _contactsList)
            Log.d("ViewModelContacts", "Contacts saved successfully to ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("ViewModelContacts", "Error saving JSON", e)
        }
    }

    fun loadAll() {
        Log.d("ViewModelContacts", "Loading contacts from JSON")
        if (file.exists()) {
            try {
                val loadedContacts = mapper.readValue(file, Array<ContactModel>::class.java).toMutableList()
                _contactsList.clear()
                _contactsList.addAll(loadedContacts)
                Log.d("ViewModelContacts", "Loaded ${_contactsList.size} contacts")
            } catch (e: Exception) {
                Log.e("ViewModelContacts", "Error reading JSON", e)
            }
        } else {
            Log.d("ViewModelContacts", "No contacts.json found, initializing with empty list")
            // Optionally, add initial hardcoded contacts here if desired
        }
    }

    fun toggleExpand(contactId: Int) {
        val index = _contactsList.indexOfFirst { it.id == contactId }
        if (index != -1) {
            val contact = _contactsList[index]
            // Since isExpanded isn't part of the persistent model, we'll handle it differently
            // For now, we'll assume it's not saved to JSON; UI state should be managed in composables
            Log.d("ViewModelContacts", "Toggling expand for contact $contactId (UI state not persisted)")
        }
    }

    fun findByName(query: String) {
        filteredContacts.clear()
        if (query.isNotEmpty()) {
            filteredContacts.addAll(
                _contactsList.filter { it.name.contains(query, ignoreCase = true) }
            )
        }
    }

    fun getAllContacts(): List<ContactModel> {
        return _contactsList.toList() // Return a copy to avoid external modification
    }
}