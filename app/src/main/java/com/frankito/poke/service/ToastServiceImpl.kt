package com.frankito.poke.service

import com.frankito.domain.models.toast.ToastData
import com.frankito.domain.services.ToastService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull

class ToastServiceImpl : ToastService {
    @ExperimentalCoroutinesApi
    private var channel: BroadcastChannel<ToastData?> = ConflatedBroadcastChannel(null)

    @ExperimentalCoroutinesApi
    @FlowPreview
    override val toastMessage: Flow<ToastData>
        get() = channel.openSubscription().consumeAsFlow().drop(1).filterNotNull()

    @ExperimentalCoroutinesApi
    override fun showToast(toastData: ToastData) {
        channel.offer(toastData)
    }
}