package com.example.final_project_assignment_hansenbillyramades.data.source.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getUserInfo(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}

// current user adalah data yang berisikan dan simpan data pengguna, berhasil login