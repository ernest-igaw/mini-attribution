package com.dfinery.attribution.api.repository

import com.dfinery.attribution.common.entity.Adtouch
import org.jetbrains.annotations.TestOnly
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class AdtouchRepository(
    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient,
    @Value("\${variables.adtouch.dynamodb.name}")
    private val tableName: String
) {
    private val table: DynamoDbTable<Adtouch>
        get() = dynamoDbEnhancedClient.table(
            tableName,
            TableSchema.fromBean(Adtouch::class.java)
        )

    fun save(adtouch: Adtouch) {
        table.putItem(adtouch)
    }

    @TestOnly
    fun deleteAllItems() {
        table.scan().items().forEach { table.deleteItem(it) }
    }
}