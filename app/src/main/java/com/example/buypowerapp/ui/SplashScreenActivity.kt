package com.example.buypowerapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.buypowerapp.R
import com.example.buypowerapp.databinding.ActivitySplashScreenBinding
import com.example.buypowerapp.util.Constant

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivitySplashScreenBinding

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        fullScreenMode()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spannable()
        handlerPostDelay()
    }

    private fun spannable() {
        val spannable = SpannableStringBuilder(getString(R.string.buy_power_ng_splash_screen))
        spannable.setSpan(
            ForegroundColorSpan(this.getColor(R.color.holo_green_light)),
            Constant.START_INDEX,
            Constant.END_INDEX,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.buyPowerText.text = spannable
    }

    private fun fullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun handlerPostDelay() {
        handler.postDelayed({
                val intent = Intent(
                    this,
                    WelcomeBackActivity::class.java
                )
                startActivity(intent)
            },
            Constant.DELAY_MILLIS
        )
    }
}