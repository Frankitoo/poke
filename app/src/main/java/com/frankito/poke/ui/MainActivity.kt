package com.frankito.poke.ui

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.frankito.domain.models.toast.ToastData
import com.frankito.domain.models.toast.ToastDuration
import com.frankito.poke.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)

            val decor: View = window.decorView
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        findNavController(R.id.nav_host_fragment).apply {
            viewModel.registerNavController(this)
        }

        viewModel.backButtonVisibility.observe(this) {
            backButton.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        viewModel.toast.observe(this, toastObserver)

        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private val toastObserver by lazy {
        Observer<ToastData> { toast ->
            Toast.makeText(
                this, toast.message ?: "",
                if (toast.duration == ToastDuration.LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            ).show()
        }
    }
}