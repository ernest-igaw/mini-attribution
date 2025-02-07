package com.dfinery.attribution.common.dto

import com.dfinery.attribution.common.util.uuid.UUIDGenerator

data class ItemDTO(
    val id: String? = UUIDGenerator.createRandomPartitionKey(),
    val productId: String,
    val productName: String,
    val productPrice: String
)