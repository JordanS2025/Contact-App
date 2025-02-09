package com.example.contactapp.models

data class BusinessModel(
    val webURL: String,
    val myOpinionRating: Int,
    val daysOpen: List<Boolean>, // Array of 7 Boolean values
    val businessType: String // Additional property
)  {
    override fun toString(): String {
        return "BusinessModel(${super.toString()}, webURL='$webURL', myOpinionRating=$myOpinionRating, daysOpen=$daysOpen, businessType='$businessType')"
    }
}