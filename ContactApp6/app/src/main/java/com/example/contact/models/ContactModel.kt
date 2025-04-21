package com.example.contact.models

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PersonModel::class, name = "Person"),
    JsonSubTypes.Type(value = BusinessModel::class, name = "Business")
)
abstract class ContactModel(
    open val id: Int,
    open val name: String,
    open val email: String,
    open val phoneNumber: String,
    open val addressStreet: String,
    open val addressCity: String,
    open val addressState: String,
    open val addressPostalCode: String,
    open val isFavorite: Boolean? = null,
    open val lastUsed: Long? = null,
    open val groups: List<String>? = null
)
