package com.dfinery.attribution.entity

import jakarta.persistence.*
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@Entity
@DynamoDbBean
data class Profile (
    @Id
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("adId")
    val adId: String = "",
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("firstOpenLogId")
    val firstOpenLogId: String = "",
    @get:DynamoDbAttribute("adKey")
    val adKey: String? = null,
    @get:DynamoDbAttribute("trackerId")
    val trackerId: String? = null
)