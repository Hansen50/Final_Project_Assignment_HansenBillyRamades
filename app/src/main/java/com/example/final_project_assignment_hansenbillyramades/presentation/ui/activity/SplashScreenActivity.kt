package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.preferences.PreferenceDataStore
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(1500L)
            val isLoggedIn = preferenceDataStore.isUserLoggedIn()
            val isOnboarded = preferenceDataStore.getUserOnboarded()

            Log.d("SplashScreen", "isLoggedIn: $isLoggedIn, isOnboarded: $isOnboarded")

            val nextActivity = when {
                isLoggedIn && isOnboarded -> MainActivity::class.java
                isLoggedIn && !isOnboarded -> OnBoardActivity::class.java
                else -> LoginActivity::class.java
            }

            val intent = Intent(this@SplashScreenActivity, nextActivity)
            startActivity(intent)
            finish()
        }
    }
}
