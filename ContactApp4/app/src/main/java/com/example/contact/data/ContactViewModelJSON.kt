package com.example.contact.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.contact.models.BusinessModel
import com.example.contact.models.ContactModel
import com.example.contact.models.PersonModel
import com.example.contact.data.ContactViewModelInterface
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File


class ContactViewModelJSON(context: Context) : ViewModel(), ContactViewModelInterface {
    private val file = File(context.filesDir, "contacts.json")
    private val mapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        // Disable type information requirement for arrays
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // Allow JSON arrays to be deserialized to Java arrays without type info
        configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    }

    override var contactsList: MutableList<ContactModel> = mutableStateListOf()

    init {
        loadContactsFromJSON()
    }

    override fun loadContactsFromJSON() {
        Log.d("ContactViewModelJSON", "Attempting to load contacts from ${file.absolutePath}")
        if (file.exists()) {
            try {
                val jsonContent = file.readText()
                Log.d("ContactViewModelJSON", "JSON content: $jsonContent")

                // Convert JSON to List of LinkedHashMap
                val jsonList = mapper.readValue(jsonContent, object : TypeReference<List<LinkedHashMap<String, Any?>>>() {})

                contactsList.clear()

                // Process each map and create appropriate ContactModel objects
                jsonList.forEach { item ->
                    val id = (item["id"] as? Number)?.toInt() ?: 0
                    val name = item["name"] as? String ?: ""
                    val email = item["email"] as? String ?: ""
                    val phoneNumber = item["phoneNumber"] as? String ?: ""
                    val addressStreet = item["addressStreet"] as? String ?: ""
                    val addressCity = item["addressCity"] as? String ?: ""
                    val addressState = item["addressState"] as? String ?: ""
                    val addressPostalCode = item["addressPostalCode"] as? String ?: ""

                    // Determine if it's a person or business based on existence of specific fields
                    val contact = if (item.containsKey("pictureURL") || item.containsKey("isFamilyMember") || item.containsKey("dateOfBirth")) {
                        // It's a PersonModel
                        val pictureURL = item["pictureURL"] as? String ?: ""
                        val isFamilyMember = item["isFamilyMember"] as? Boolean ?: false
                        val dateOfBirth = item["dateOfBirth"] as? String ?: ""

                        PersonModel(
                            id = id,
                            name = name,
                            email = email,
                            phoneNumber = phoneNumber,
                            addressStreet = addressStreet,
                            addressCity = addressCity,
                            addressState = addressState,
                            addressPostalCode = addressPostalCode,
                            pictureURL = pictureURL,
                            isFamilyMember = isFamilyMember,
                            dateOfBirth = dateOfBirth
                        )
                    } else {
                        // It's a BusinessModel
                        val webURL = item["webURL"] as? String ?: ""
                        val myOpinionRating = (item["myOpinionRating"] as? Number)?.toInt() ?: 0

                        // Parse days open (handling different possible formats)
                        val daysOpen = when (val daysValue = item["daysOpen"]) {
                            is List<*> -> daysValue.map { it as? Boolean ?: false }
                            is Array<*> -> daysValue.map { it as? Boolean ?: false }
                            null -> List(7) { false }
                            else -> List(7) { false }
                        }

                        val businessType = item["businessType"] as? String ?: "General"

                        BusinessModel(
                            id = id,
                            name = name,
                            email = email,
                            phoneNumber = phoneNumber,
                            addressStreet = addressStreet,
                            addressCity = addressCity,
                            addressState = addressState,
                            addressPostalCode = addressPostalCode,
                            webURL = webURL,
                            myOpinionRating = myOpinionRating,
                            daysOpen = daysOpen,
                            businessType = businessType
                        )
                    }

                    contactsList.add(contact)
                }

                Log.d("ContactViewModelJSON", "Loaded ${contactsList.size} contacts: $contactsList")
            } catch (e: Exception) {
                Log.e("ContactViewModelJSON", "Error loading JSON", e)
                e.printStackTrace() // Print full stack trace for debugging
            }
        } else {
            Log.d("ContactViewModelJSON", "No contacts file found at ${file.absolutePath}")
        }
    }

    override fun saveContactsToJSON() {
        Log.d("ContactViewModelJSON", "Saving ${contactsList.size} contacts to ${file.absolutePath}")
        try {
            // First convert each contact to a Map with type information
            val contactMaps = contactsList.map { contact ->
                val map = mutableMapOf<String, Any?>()

                // Add common properties
                map["id"] = contact.id
                map["name"] = contact.name
                map["email"] = contact.email
                map["phoneNumber"] = contact.phoneNumber
                map["addressStreet"] = contact.addressStreet
                map["addressCity"] = contact.addressCity
                map["addressState"] = contact.addressState
                map["addressPostalCode"] = contact.addressPostalCode

                // Add specific properties based on type
                when (contact) {
                    is PersonModel -> {
                        map["pictureURL"] = contact.pictureURL
                        map["isFamilyMember"] = contact.isFamilyMember
                        map["dateOfBirth"] = contact.dateOfBirth
                    }
                    is BusinessModel -> {
                        map["webURL"] = contact.webURL
                        map["myOpinionRating"] = contact.myOpinionRating
                        map["daysOpen"] = contact.daysOpen
                        map["businessType"] = contact.businessType
                    }
                }

                map
            }

            // Now write this list of maps to the file
            mapper.writeValue(file, contactMaps)
            val savedContent = file.readText()
            Log.d("ContactViewModelJSON", "Saved JSON content: $savedContent")
            Log.d("ContactViewModelJSON", "Contacts saved successfully")
        } catch (e: Exception) {
            Log.e("ContactViewModelJSON", "Error saving JSON", e)
            e.printStackTrace() // Print full stack trace for debugging
        }
    }

    override fun addContact(contact: ContactModel) {
        Log.d("ContactViewModelJSON", "Adding contact of type: ${contact::class.simpleName}")
        val newId = (contactsList.maxOfOrNull { it.id } ?: 0) + 1
        val newContact = when (contact) {
            is BusinessModel -> contact.copy(id = newId)
            is PersonModel -> contact.copy(id = newId)
            else -> throw IllegalArgumentException("Unknown contact type: ${contact::class.simpleName}")
        }
        contactsList.add(newContact)
        saveContactsToJSON()
    }

    override fun removeContact(contact: ContactModel) {
        contactsList.remove(contact)
        saveContactsToJSON()
    }
}