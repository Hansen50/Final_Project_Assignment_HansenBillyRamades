package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.PreferenceDataStore
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.LoginActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.MainActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.OnBoardActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : ViewModel() {

    private val _nextActivity = MutableStateFlow<Class<*>>(LoginActivity::class.java)
    val nextActivity = _nextActivity.asStateFlow()

    fun checkStatusLoginAndOnBoard() {
        viewModelScope.launch {
            delay(5000L)
            val isLoggedIn = preferenceDataStore.isUserLoggedIn()
            val isOnboarded = preferenceDataStore.getUserOnboarded()

            _nextActivity.value = when {
                isLoggedIn && isOnboarded -> MainActivity::class.java
                isLoggedIn && !isOnboarded -> OnBoardActivity::class.java
                else -> LoginActivity::class.java
            }
        }
    }
}