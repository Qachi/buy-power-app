package com.example.buypowerapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.buypowerapp.R
import com.example.buypowerapp.databinding.ActivityRegisterBinding
import com.example.buypowerapp.util.Constant
import com.example.buypowerapp.util.showToast
import com.example.buypowerapp.viewmodel.AuthViewModel
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val authViewModel by viewModels<AuthViewModel>()

    companion object {
        const val CREATE_USER = "Create user"
        const val CREATE_USER_FAILURE = "Create user failed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        buyPowerText()
        registerOnclickListener()
        loginOnclickListener()
        loginButtonBackgroundColor()
    }

    private fun register(userFullName: String) {
        val firstName = userFullName.split(" ")[0]
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        intent.putExtra(
            Constant.REGISTERED_USER_NAME,
            firstName
        )
        startActivity(intent)
        finish()
    }

    private fun statusBarColor() {
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.hint_foreground_holo_light
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

    @SuppressLint("ShowToast")
    private fun registerOnclickListener() {
        binding.regToContinue.setOnClickListener {
            val userFullNameEditText = binding.userFullName
            val userFullName = userFullNameEditText.text.toString().trim()
            val userEmail = binding.userEmail.text.toString().trim()
            val userPhoneNumber = binding.phoneNumber.text.toString().trim()
            val userPassword = binding.passwordReg.text.toString().trim()
            val userConfirmPassword = binding.confirmPassword.text.toString().trim()

            if (userFullName.isEmpty()) {
                showToast(getString(R.string.enter_full_name))
                return@setOnClickListener
            }
            if (userEmail.isEmpty()) {
                showToast(getString(R.string.enter_email_address))
                return@setOnClickListener
            } else if (userPhoneNumber.length != Constant.PHONE_NUMBER) {
                showToast(getString(R.string.enter_correct_phonenumber))
                return@setOnClickListener
            } else if (userPassword.length <= Constant.PASSWORD) {
                showToast( getString(R.string.password_is_short))
                return@setOnClickListener
            }
            if (userPassword != userConfirmPassword) {
                showToast( getString(R.string.password_not_match))
                return@setOnClickListener
            }
            createUserWithEmailAndPassword(
                userEmail,
                userPassword,
                userFullName,
                userPhoneNumber
            )
        }
    }

    private fun loginOnclickListener() {
        binding.loginButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    WelcomeBackActivity::class.java
                )
            )
            finish()
        }
    }

    private fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String
    ) {
        authViewModel.createUserWithEmailAndPassword(
            email,
            password,
            fullName,
            phoneNumber
        ) { success, exception, errorMessage ->
            run {
                if (success) {
                    showToast(getString(R.string.signUp_successful))
                    register(fullName)
                } else {
                    errorMessage?.let {
                        if (exception is FirebaseAuthWeakPasswordException) {
                            val errorReason = getString(R.string.password_6_characters)
                            showToast(errorReason)
                        } else {
                            showToast(errorMessage)
                        }
                    }
                    Log.e(CREATE_USER, "$CREATE_USER_FAILURE: $errorMessage")
                }
            }
        }
    }

    private fun loginButtonBackgroundColor() {
        binding.loginButton.setBackgroundColor(this.getColor(R.color.primary))
    }
}
