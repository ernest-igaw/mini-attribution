package com.dfinery.attribution.entity

import jakarta.persistence.*

@Entity
@Table(name = "Adtouch")
data class Adtouch (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String?,
    val adKey: String,
    val trackerId: String,
    val createdAt: String
)