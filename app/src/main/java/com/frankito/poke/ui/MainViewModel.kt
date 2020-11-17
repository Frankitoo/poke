package com.frankito.poke.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.frankito.domain.models.toast.ToastData
import com.frankito.domain.services.ToastService
import com.frankito.poke.R
import com.frankito.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

class MainViewModel(
    toastService: ToastService,
) : BaseViewModel() {
    private val currentDestination: LiveData<NavDestination> = object : LiveData<NavDestination>() {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            postValue(destination)
        }

        override fun onActive() {
            navController?.addOnDestinationChangedListener(listener)
            navController?.currentDestination?.let { postValue(it) }
        }

        override fun onInactive() {
            navController?.removeOnDestinationChangedListener(listener)
        }
    }

    val toast by liveData<ToastData> {
        toastService.toastMessage.collectLatest { emit(it) }
    }

    val showBackButton = currentDestination.map { destination ->
        return@map when (destination.id) {
            in NON_BACK_SCREENS -> false
            else -> true
        }
    }

    companion object {
        private val NON_BACK_SCREENS = setOf(
            R.id.PokemonListFragment,
        )
    }
}