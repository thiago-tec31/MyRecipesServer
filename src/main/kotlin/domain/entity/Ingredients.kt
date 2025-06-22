package com.br.domain.entity

import com.br.domain.extensions.formatInstantForBrazilian
import kotlinx.datetime.Clock
import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation
import org.bson.types.ObjectId

data class Ingredients(
    val name: String,
    val quantity: String,
    val recipeId: String,
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    override val id: String = ObjectId().toHexString(),
    override val createdAt: String = Clock.System.now().formatInstantForBrazilian(),
): Basic()
