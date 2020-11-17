package com.frankito.data.api.mappers

import com.frankito.data.api.models.response.PokemonDetailResponse
import com.frankito.data.api.models.response.PokemonListItemResponse
import com.frankito.domain.models.pokemon.PokemonDetail
import com.frankito.domain.models.pokemon.PokemonListItem

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
        height = height,
        weight = weight,
        imageUrl = imageUrl,
    )
}

fun getPokemonId(url: String): Int =
    url.split("/".toRegex()).dropLast(1).last().toInt()

fun getImageUrl(id: Int): String {
    return "https://pokeres.bastionbot.org/images/pokemon/$id.png"
}