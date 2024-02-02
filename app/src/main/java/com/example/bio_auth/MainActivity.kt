package com.example.bio_auth

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var biometricAuthBTN: MaterialButton
    private lateinit var emailEDT: TextInputEditText
    private lateinit var passwordEDT: TextInputEditText
    private lateinit var loginButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uiViewsID()
        setOnClickEvent()


    }

    private fun uiViewsID() {
        emailEDT = findViewById(R.id.emailEDT)
        passwordEDT = findViewById(R.id.passwordEDT)
        loginButton = findViewById(R.id.loginButton)
        biometricAuthBTN = findViewById(R.id.biometricAuthBTN)

    }

    private fun setOnClickEvent() {

        loginButton.setOnClickListener {
            val pattern = Patterns.EMAIL_ADDRESS
            val email = emailEDT.text.toString()
            val password = passwordEDT.text.toString()
            if (email.isEmpty() || email.isBlank() || !pattern.matcher(email).matches()) {
                showMessage("Enter Email")
                return@setOnClickListener
            }
            if (password.isEmpty() || password.isBlank()) {
                showMessage("Enter Password")
                return@setOnClickListener
            }
            startActivity(Intent(this, HomeActivity::class.java))

        }
        biometricAuthBTN.setOnClickListener {
            if (isBiometricSupported()) {
                showBiometricPrompt()
            }
        }

    }

    private fun isBiometricSupported(): Boolean {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {

            BiometricManager.BIOMETRIC_SUCCESS -> {
                showAlertDialog("Biometric authentication is Available")
                return true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showAlertDialog("Biometric Hardware is not Available")
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showAlertDialog("Biometric authentication is currently Unavailable")
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showAlertDialog("No biometric credentials are Enrolled")
                return false
            }

            else -> {
                // Biometric status unknown or another error occurred
                return false
            }
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error
                    showAlertDialog("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Handle authentication success
                    showAlertDialog("Authentication succeeded!")

                    moveHomePage()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle authentication failure
                    showAlertDialog("Biometric is registered but not authorised with Current User.")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }


    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Biometric Authentication")
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun moveHomePage() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}