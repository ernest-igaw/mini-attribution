package com.dfinery.attribution.common.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@Entity
@DynamoDbBean
data class Adtouch(
    @Id
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("adKey")
    var adKey: String = "",
    @get:DynamoDbAttribute("trackerId")
    var trackerId: String = "",
    @get:DynamoDbAttribute("createdAt")
    var createdAt: String = ""
)