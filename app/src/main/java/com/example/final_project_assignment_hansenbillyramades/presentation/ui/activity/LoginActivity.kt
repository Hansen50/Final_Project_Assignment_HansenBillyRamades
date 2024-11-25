package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.preferences.PreferenceDataStore
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityLoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignInLayout.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        binding.googleIcon.isVisible = false
        binding.btnText.isVisible = false
        binding.loadingLayout.isVisible = true

        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("223888826256-pn2kojspqlsek8ho7kdo8nglojf3aeqe.apps.googleusercontent.com")
            .build()


        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                resetButtonState()
                Log.d("Error", e.message.toString())
            }
        }
    }


    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("TAG", "Received an invalid Google ID token response", e)
                    }
                } else {
                    Log.e("TAG", "Unexpected type of credential")
                }
            }

            else -> {
                Log.e("TAG", "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        if (idToken != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lifecycleScope.launch {
                            preferenceDataStore.setUserLoggedIn(true)
                            val isOnboarded = preferenceDataStore.getUserOnboarded()
                            val intent = if (isOnboarded) {
                                Intent(this@LoginActivity, MainActivity::class.java)
                            } else {
                                Intent(this@LoginActivity, OnBoardActivity::class.java)
                            }

                            startActivity(intent)
                            finish()
                            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Failed, Please check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    resetButtonState()
                }
        } else {
            Log.e("TAG", "ID Token is null")
            resetButtonState()
        }
    }


    private fun resetButtonState() {
        binding.loadingLayout.isVisible = false
        binding.googleIcon.isVisible = true
        binding.btnText.isVisible = true
    }


}