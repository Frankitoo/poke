package com.frankito.data.api.mappers

import com.frankito.data.api.models.response.PokemonDetailResponse
import com.frankito.data.api.models.response.PokemonListItemResponse
import com.frankito.domain.models.pokemon.*

fun PokemonListItemResponse.toDomainModel(): PokemonListItem {
    val pokemonId = getPokemonId(url)
    val imageUrl = getImageUrl(pokemonId)
    return PokemonListItem(
        id = pokemonId,
        name = name,
        imageUrl = imageUrl
    )
}

fun PokemonDetailResponse.toDomainModel(): PokemonDetail {
    val imageUrl = getImageUrl(id)
    return PokemonDetail(
        id = id,
        name = name,
        // The height and weigh values are in decimeter and hectogram
        height = DoubleWithUnit(height.toDouble() / 10, Units.heightUnit),
        weight = IntWithUnit(weight / 10, Units.weightUnit),
        experience = IntWithUnit(experience, Units.expUnit),
        imageUrl = imageUrl,
        types = types.map { it.type.name },
        abilities = abilities.map { it.ability.name }
    )
}

fun getPokemonId(url: String): Int =
    url.split("/".toRegex()).dropLast(1).last().toInt()

fun getImageUrl(id: Int): String {
    return "https://pokeres.bastionbot.org/images/pokemon/$id.png"
}