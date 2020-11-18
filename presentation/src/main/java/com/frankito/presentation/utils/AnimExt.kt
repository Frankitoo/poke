package com.frankito.presentation.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.frankito.presentation.R

fun View.startRotatedAnimation(context: Context) {
    val rotation = AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely)
    rotation.fillAfter = true
    this.startAnimation(rotation)
}