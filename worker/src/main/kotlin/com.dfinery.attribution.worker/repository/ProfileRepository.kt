package com.dfinery.attribution.worker.repository

import com.dfinery.attribution.common.entity.Profile
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
class ProfileRepository(
    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient,
    @Value("\${variables.profile.dynamodb.name}")
    private val tableName: String
) {
    private val table: DynamoDbTable<Profile>
        get() = dynamoDbEnhancedClient.table(
            tableName,
            TableSchema.fromBean(Profile::class.java)
        )

    fun findByAdId(adId: String?): Profile? {
        adId ?: return null

        val queryConditional = QueryConditional
            .keyEqualTo(
                Key.builder()
                    .partitionValue(adId)
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

    fun save(profile: Profile) {
        table.putItem(profile)
    }

    fun delete(profile: Profile) {
        table.deleteItem(profile)
    }
}