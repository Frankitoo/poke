package com.frankito.domain.models.toast

data class ToastData(
    val duration: ToastDuration?,
    val message: String?,
    val messageResId: Int?,
    val iconResId: Int?
) {
    companion object {
        fun ofContent(message: String?, iconResId: Int?) = ToastData(
            duration = ToastDuration.LONG,
            message = message,
            iconResId = iconResId,
            messageResId = null
        )
    }
}