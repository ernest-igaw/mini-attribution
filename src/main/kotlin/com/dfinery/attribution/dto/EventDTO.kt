package com.dfinery.attribution.dto

data class EventDTO (
    val id: String?,
    val adId: String,
    val adKey: String,
    val trackerId: String,
    val items: List<String>? = null
)