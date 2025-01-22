package com.dfinery.attribution.entity

import jakarta.persistence.*

@Entity
@Table(name = "Event")
data class Event (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String?,
    val adId: String,
    val adKey: String,
    val trackerId: String
)