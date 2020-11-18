package com.frankito.presentation.features.pokemondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.frankito.domain.error.ErrorHandler
import com.frankito.domain.models.pokemon.PokemonDetail
import com.frankito.domain.repositories.PokemonRepository
import com.frankito.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    private val pokemonRepository: PokemonRepository,
    private val errorHandler: ErrorHandler,
) : BaseViewModel() {

    private val pokemonDetailMutableLiveData: MutableLiveData<PokemonDetail> = MutableLiveData()
    val pokemonDetailLiveData: LiveData<PokemonDetail> = pokemonDetailMutableLiveData

    private val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loadingLiveData: LiveData<Boolean> = loadingMutableLiveData

    fun fetchPokemon(pokemonName: String) {
        loadingMutableLiveData.value = true
        viewModelScope.launch(errorHandler) {
            val pokemonDetail = pokemonRepository.fetchPokemon(pokemonName)
            pokemonDetailMutableLiveData.postValue(pokemonDetail)
        }.invokeOnCompletion {
            loadingMutableLiveData.value = false
        }
    }
}