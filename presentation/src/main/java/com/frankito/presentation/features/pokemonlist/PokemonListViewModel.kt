package com.frankito.presentation.features.pokemonlist

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.frankito.domain.models.pokemon.PokemonListItem
import com.frankito.domain.models.toast.ToastData
import com.frankito.domain.repositories.PokemonRepository
import com.frankito.domain.services.ConnectionService
import com.frankito.domain.services.ToastService
import com.frankito.presentation.R
import com.frankito.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.Flow

class PokemonListViewModel(
    private val pokemonRepository: PokemonRepository,
    private val toastService: ToastService,
    private val connectionService: ConnectionService,
    private val applicationContext: Context,
) : BaseViewModel() {

    fun fetchPokemons(): Flow<PagingData<PokemonListItem>> {
        return pokemonRepository.fetchPokemons().cachedIn(viewModelScope)
    }

    fun onPokemonSelected(pokemonListItem: PokemonListItem) {
        if (connectionService.isConnected()) {
            navController?.navigate(
                PokemonListFragmentDirections.actionPokemonSelected(
                    pokemonListItem.name
                )
            )
        } else {
            toastService.showToast(
                ToastData.ofContent(
                    applicationContext.getString(R.string.common_network_unavailable)
                )
            )
        }
    }
}
