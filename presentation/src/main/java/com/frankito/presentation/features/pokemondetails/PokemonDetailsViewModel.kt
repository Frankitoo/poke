package com.frankito.presentation.features.pokemondetails

import com.frankito.domain.models.pokemon.PokemonDetail
import com.frankito.domain.repositories.PokemonRepository
import com.frankito.presentation.common.BaseViewModel

class PokemonDetailsViewModel(
    private val pokemonRepository: PokemonRepository
) : BaseViewModel() {

    val pokemonDetail by liveData<PokemonDetail> {
        val pokemonName = arguments.value?.let { PokemonDetailsFragmentArgs.fromBundle(it).pokemonName }
        pokemonName?.let {
            emit(pokemonRepository.getPokemon(pokemonName))
        }
    }
}