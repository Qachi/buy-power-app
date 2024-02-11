package com.example.buypowerapp

import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.buypowerapp.util.Constant
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForgotPasswordDialog(context: Context) {
    private val alertDialog: AlertDialog
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.forget_password_customlayout, null)

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(customView)
        alertDialogBuilder.setTitle(Constant.ALERTDIALOG_TITLE)
        alertDialogBuilder.setMessage(Constant.ALERTDIALOG_MESSAGE)

        alertDialog = alertDialogBuilder.create()

        val userEmail = customView.findViewById(R.id.passwordReset_Email) as TextView
        val resetButton = customView.findViewById(R.id.button_reset) as Button
        val cancelButton = customView.findViewById(R.id.button_cancel) as Button
        cancelButton.setBackgroundColor(ContextCompat.getColor(context, R.color.primary))

        resetButton.setOnClickListener {
            val userInput = userEmail.text.toString().trim()

            if (userInput.isEmpty()) {
                showToast(context,context.getString(R.string.enter_emailAddress))
            } else {
                // Check if the user with this email exists
                firebaseAuth.fetchSignInMethodsForEmail(userInput)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            if (signInMethods.isNullOrEmpty()) {
                                showToast(context,context.getString(R.string.email_not_registered))
                            } else {
                                firebaseAuth.sendPasswordResetEmail(userInput)
                                    .addOnCompleteListener {
                                        showToast(context,context.getString(R.string.reset_password_email_sent))
                                        alertDialog.dismiss() // Dismiss the custom AlertDialog when resetting password
                                    }
                            }
                        } else {
                           showToast(context,context.getString(R.string.error_checking_email_existence))
                        }
                    }
            }
        }
        cancelButton.setOnClickListener {
            alertDialog.dismiss() // Dismiss the custom AlertDialog when the negative button is clicked
        }
    }

    fun show() {
        alertDialog.show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun showToast(context: Context, message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}