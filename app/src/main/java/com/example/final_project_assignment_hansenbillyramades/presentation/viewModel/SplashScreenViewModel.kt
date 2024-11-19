package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _nextActivity = MutableLiveData<Class<*>>(LoginActivity::class.java) // Menggunakan LiveData agar bisa ter-observe
    val nextActivity: LiveData<Class<*>> = _nextActivity

    fun checkStatusLoginAndOnBoard() {
        viewModelScope.launch {
            delay(3000L) // Waktu delay untuk splash screen
            val isLoggedIn = preferenceDataStore.isUserLoggedIn()
            val isOnboarded = preferenceDataStore.getUserOnboarded()

            _nextActivity.value = when {
                isLoggedIn && isOnboarded -> MainActivity::class.java // Sudah login dan onboarded
                isLoggedIn && !isOnboarded -> OnBoardActivity::class.java // Sudah login tapi belum onboarded
                else -> LoginActivity::class.java // Belum login
            }
        }
    }
}

