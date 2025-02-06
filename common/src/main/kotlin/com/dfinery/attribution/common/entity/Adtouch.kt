package com.dfinery.attribution.common.entity

import com.dfinery.attribution.common.util.uuid.UUIDGenerator
import jakarta.persistence.*
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*

@Entity
@DynamoDbBean
data class Adtouch (
    @Id
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("adKey")
    var adKey: String = UUIDGenerator.createRandomPartitionKey(),
    @get:DynamoDbAttribute("trackerId")
    var trackerId: String = "",
    @get:DynamoDbAttribute("createdAt")
    var createdAt: String? = null
)