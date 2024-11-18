package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivitySplashScreenBinding
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.nextActivity.collectLatest { nextActivity ->
                Log.d("SplashScreenActivity", "Navigating to: $nextActivity")
                val intent = Intent(this@SplashScreenActivity, nextActivity)
                startActivity(intent)
                finish()
            }
        }
        viewModel.checkStatusLoginAndOnBoard()
    }
}