package com.example.buypowerapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.buypowerapp.ForgotPasswordDialog
import com.example.buypowerapp.R
import com.example.buypowerapp.databinding.ActivityWelcomeBackBinding
import com.example.buypowerapp.util.showToast
import com.example.buypowerapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeBackActivity : AppCompatActivity() {

    private var i = 0
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityWelcomeBackBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        buyPowerText()
        setupRegisterButton()
        forgotPassword()
        continueToBuyOnclickListener()
        forgotYourPasswordButtonBackgroundColor()
        registerButtonBackgroundColor()
    }

    override fun onStart() {
        super.onStart()
        if (authViewModel.firebaseAuth.currentUser != null) {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
            finish()
        }
    }

    private fun registerOnclickListener() {
        startActivity(
            Intent(
                this,
                RegisterActivity::class.java
            )
        )
    }

    private fun buyPowerText() {
        val htmlString = getString(R.string.buy_power_html_string)
        val coloredHtmlString = String.format(
            htmlString,
            getColor(R.color.primary),
            getColor(R.color.holo_green_light)
        )

        val spannedText = HtmlCompat.fromHtml(
            coloredHtmlString,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.buyPowerText.text = spannedText
    }

    private fun continueToBuyOnclickListener() {
        binding.continueToBuyElectricity.setOnClickListener {
            binding.progressBarWelcome.visibility = View.VISIBLE
            i = binding.progressBarWelcome.progress

            Thread {
                while (i < 60) {
                    i += 1
                    handler.post {
                        binding.progressBarWelcome.progress = i
                    }
                    try {
                        Thread.sleep(60)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                binding.progressBarWelcome.visibility = View.INVISIBLE
            }.start()

            val userEmail = binding.userEmail.text.toString()
            val userPassword = binding.userPassword.text.toString()
            if (userEmail.isEmpty()) {
                showToast(getString(R.string.enter_emailAddress))
                return@setOnClickListener
            }
            if (userPassword.isEmpty()) {
                showToast( getString(R.string.enter_password))
            } else if (userPassword.length <= 3) {
                showToast( getString(R.string.password_is_short))
            } else {
                signInWithEmailAndPassword(userEmail, userPassword)
            }
        }
    }

    private fun setupRegisterButton() {
        binding.register.setOnClickListener {
            registerOnclickListener()
        }
    }

    private fun forgotPassword() {
        binding.forgotYourPassword.setOnClickListener {
            forgotYourPasswordOnclickListener()
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        authViewModel.signInWithEmailAndPassword(email, password)
        { success, errorMessage ->
            if (success) {
                showToast(getString(R.string.signed_in_successfully))
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
                finish()
            } else {
                errorMessage?.let {
                  showToast(it)
                }
            }
        }
    }

    private fun forgotYourPasswordButtonBackgroundColor() {
        binding.forgotYourPassword.setBackgroundColor(this.getColor(R.color.qachi))
    }

    private fun registerButtonBackgroundColor() {
        binding.register.setBackgroundColor(this.getColor(R.color.primary))
    }

    private fun statusBarColor() {
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.hint_foreground_holo_light
        )
    }

    private fun forgotYourPasswordOnclickListener() {
        val forgotPasswordDialog = ForgotPasswordDialog(this)
        forgotPasswordDialog.show()
    }
}
