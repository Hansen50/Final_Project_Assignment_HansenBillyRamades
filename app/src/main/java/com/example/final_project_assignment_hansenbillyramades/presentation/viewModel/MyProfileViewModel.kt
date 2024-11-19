package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.PreferenceDataStore
import com.example.final_project_assignment_hansenbillyramades.domain.model.UserState
import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetUserInfoUseCase
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.LogoutUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val preferenceDataStore: PreferenceDataStore

) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun getUserInfo() {
        viewModelScope.launch {
            try {
                val user = getUserInfoUseCase()
                if (user != null) {
                    _userState.value = UserState.Success(user)
                } else {
                    _userState.value = UserState.Error("User not logged in")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Failed to fetch user info: ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceDataStore.setUserLoggedIn(false)
            logoutUseCase
        }
    }
}