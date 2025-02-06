package com.dfinery.attribution.common.entity

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
    var adId: String = "",
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("firstOpenLogId")
    var firstOpenLogId: String = "",
    @get:DynamoDbAttribute("adKey")
    var adKey: String? = null,
    @get:DynamoDbAttribute("trackerId")
    var trackerId: String? = null
)