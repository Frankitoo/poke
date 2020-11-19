package com.frankito.poke.ui

import com.frankito.domain.models.toast.ToastData
import com.frankito.domain.services.BackButtonService
import com.frankito.domain.services.ToastService
import com.frankito.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.collect

class MainViewModel(
    toastService: ToastService,
    backButtonService: BackButtonService,
) : BaseViewModel() {

    val toast by liveData<ToastData> {
        toastService.toastMessage.collect { emit(it) }
    }

    val backButtonVisibility by liveData<Boolean> {
        backButtonService.isVisible.collect { emit(it) }
    }
}