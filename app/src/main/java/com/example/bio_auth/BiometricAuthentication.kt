package com.example.bio_auth

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricAuthentication {

    fun isBiometricSupported(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {

            BiometricManager.BIOMETRIC_SUCCESS -> {
                //showMessage(context, ErrorCode.BIOMETRIC_SUCCESS.message)
                return true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showMessage(context, ErrorCode.BIOMETRIC_ERROR_NO_HARDWARE.message)
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showMessage(context, ErrorCode.BIOMETRIC_ERROR_HW_UNAVAILABLE.message)
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showMessage(context, ErrorCode.BIOMETRIC_ERROR_NONE_ENROLLED.message)
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                showMessage(context, ErrorCode.BIOMETRIC_ERROR_UNSUPPORTED.message)
                return false
            }

            else -> {
                showMessage(context, ErrorCode.BIOMETRIC_STATUS_UNKNOWN.message)
                return false
            }
        }
    }

    fun showBiometricPrompt(context: FragmentActivity, title: String) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt =
            BiometricPrompt(context, ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Handle authentication error
                        showMessage(context, ErrorCode.AUTHENTICATION_ERROR.message)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        showMessage(context, ErrorCode.BIOMETRIC_SUCCESS.message)

                        context.startActivity(Intent(context, HomeActivity::class.java))

                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        // Handle authentication failure
                        showMessage(context, ErrorCode.AUTHENTICATION_FAILED.message)
                    }
                })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    enum class ErrorCode(val value: Int, val message: String) {
        BIOMETRIC_SUCCESS(0, "Biometric Success"),
        BIOMETRIC_STATUS_UNKNOWN(-1, "Biometric status Unknown"),
        BIOMETRIC_ERROR_UNSUPPORTED(-2, "Biometric Error Unsupported"),
        BIOMETRIC_ERROR_HW_UNAVAILABLE(1, "Biometric Error Hardware Unavailable"),
        BIOMETRIC_ERROR_NONE_ENROLLED(11, "Biometric Error None Enrolled"),
        BIOMETRIC_ERROR_NO_HARDWARE(12, "Biometric Error No Hardware"),
        AUTHENTICATION_FAILED(13, "Biometric is registered but not authorised with Current User."),
        AUTHENTICATION_ERROR(14, "Authentication Error");

    }

}