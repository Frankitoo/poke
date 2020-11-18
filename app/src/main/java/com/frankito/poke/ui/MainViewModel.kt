package com.frankito.poke.ui

import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.frankito.domain.models.toast.ToastData
import com.frankito.domain.services.BackButtonService
import com.frankito.domain.services.ToastService
import com.frankito.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

class MainViewModel(
    toastService: ToastService,
    backButtonService: BackButtonService,
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

    val backButtonVisibility by liveData<Boolean> {
        backButtonService.isVisible.collectLatest { emit(it) }
    }
}