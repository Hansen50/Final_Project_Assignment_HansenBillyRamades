package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(

) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            val displayName = user.displayName?: "No Name"
            val email = user.email ?: "No email available"
            val phone = user.phoneNumber ?: "No phone available"
            val photoUrl = user.photoUrl?.toString()

            _user.value = User(displayName, email, phone, photoUrl)
        } else {
            _user.value = User("User not logged in", "", "", null)
        }
    }

    fun logout() {
        auth.signOut()
    }
}