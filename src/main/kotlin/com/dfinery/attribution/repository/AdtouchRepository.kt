package com.dfinery.attribution.repository

import com.dfinery.attribution.entity.Adtouch
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.services.sqs.model.ResourceNotFoundException

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

    fun findByAdKey(adKey: String?): Adtouch? {
        adKey ?: return null

        val queryConditional = QueryConditional
            .keyEqualTo(
                Key.builder()
                    .partitionValue(adKey)
                    .build()
            )

        val queryEnhancedRequest = QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .limit(1)
            .build()

        return try {
            table.query(queryEnhancedRequest)
                .items()
                .stream()
                .findFirst()
                .orElse(null)
        } catch (e: ResourceNotFoundException) {
            null
        } catch (e: NoSuchElementException) {
            throw e
        }
    }

    fun save(adtouch: Adtouch) {
        table.putItem(adtouch)
    }
}