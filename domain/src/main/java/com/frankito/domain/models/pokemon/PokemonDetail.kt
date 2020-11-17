package com.frankito.domain.models.pokemon

class PokemonDetail (
    val id: Int,
    val name: String,
    val height: ValueWithUnit,
    val weight: ValueWithUnit,
    val experience: ValueWithUnit,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
)

data class ValueWithUnit(val value: Int, val unit: String)