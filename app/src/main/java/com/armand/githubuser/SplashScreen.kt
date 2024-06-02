package com.armand.githubuser

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        navigateToMainActivityWithDelay()
    }

    private fun navigateToMainActivityWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMainActivity()
        }, DELAY_TIME)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val DELAY_TIME = 5000L
    }
}
