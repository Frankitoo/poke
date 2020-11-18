package com.frankito.presentation.features.pokemonpager

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frankito.domain.models.pokemon.PokemonListItem
import com.frankito.domain.models.toast.ToastData
import com.frankito.domain.services.BackButtonService
import com.frankito.domain.services.ConnectionService
import com.frankito.domain.services.ToastService
import com.frankito.presentation.R
import com.frankito.presentation.common.BaseViewModel

class PokemonPagerViewModel(
    private val toastService: ToastService,
    private val connectionService: ConnectionService,
    private val backButtonService: BackButtonService,
    private val applicationContext: Context,
) : BaseViewModel() {

    private val currentItemMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val currentItemLiveData: LiveData<Int> = currentItemMutableLiveData

    private val onPokemonSelectedMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val onPokemonSelectedLiveData: LiveData<String> = onPokemonSelectedMutableLiveData

    fun onBackPressed(): Boolean {
        return if (currentItemMutableLiveData.value == 0) {
            false
        } else {
            currentItemMutableLiveData.value = currentItemMutableLiveData.value?.minus(1)
            backButtonService.invisible()
            true
        }
    }

    fun onPokemonSelected(pokemonListItem: PokemonListItem) {
        if (connectionService.isConnected()) {
            onPokemonSelectedMutableLiveData.value = pokemonListItem.name
            currentItemMutableLiveData.value = 1
            backButtonService.visible()
        } else {
            toastService.showToast(
                ToastData.ofContent(
                    applicationContext.getString(R.string.common_network_unavailable)
                )
            )
        }
    }
}