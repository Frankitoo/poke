package com.frankito.data.api.models.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class PokemonDetailResponse(
    @field:Json(name = "id") @PrimaryKey val id: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "weight") val weight: Int,
    @field:Json(name = "base_experience") val experience: Int,
    @field:Json(name = "types") val types: List<TypeResponse>
) {
    data class TypeResponse(
        @field:Json(name = "slot") val slot: Int,
        @field:Json(name = "type") val type: Type
    )

    data class Type(
        @field:Json(name = "name") val name: String
    )
}