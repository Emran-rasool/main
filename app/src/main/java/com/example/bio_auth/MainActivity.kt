package com.example.bio_auth

import android.content.Intent
import android.os.Bundle
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
            if (BiometricAuthentication.isBiometricSupported(applicationContext)) {
                BiometricAuthentication.showBiometricPrompt(
                    this@MainActivity,
                    "Biometric Authentication"
                )
            }
        }

    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}