//package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.final_project_assignment_hansenbillyramades.domain.model.UserState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class LoginViewModel @Inject constructor(
//    private val loginUseCase: LoginUseCase
//) : ViewModel() {
//
//    private val _userState = MutableLiveData<UserState>()
//    val userState: LiveData<UserState> get() = _userState
//
//    fun login(idToken: String) {
//        _userState.value = UserState.Loading  // Ketika login mulai
//
//        viewModelScope.launch {
//            try {
//                val user = loginUseCase.execute(idToken)
//                _userState.value = UserState.Success(user)  // Ketika login berhasil
//            } catch (e: Exception) {
//                _userState.value = UserState.Error(e.message ?: "Login failed")  // Ketika login gagal
//            }
//        }
//    }
//
//    fun logout() {
//        // Handle logout (misalnya clear session)
//    }
//}
//
