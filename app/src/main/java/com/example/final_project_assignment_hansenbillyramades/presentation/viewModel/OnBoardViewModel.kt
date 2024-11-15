package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.final_project_assignment_hansenbillyramades.data.source.local.PreferenceDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : ViewModel() {

    suspend fun setOnboarded(onboarded: Boolean) {
        preferenceDataStore.setOnboardedStatus(onboarded)
    }
}