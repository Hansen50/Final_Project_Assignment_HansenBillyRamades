package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyProfileBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.UserState
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.LoginActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserInfo()
        observeUserData()

        binding.btnLogout.setOnClickListener {
            dialogBoxConfirmationLogout()
        }
    }

    private fun observeUserData() {
        lifecycleScope.launch {
            viewModel.userState.collect(object : FlowCollector<UserState> {
                override suspend fun emit(value: UserState) {
                    when (value) {
                        is UserState.Error -> {
                            Toast.makeText(requireContext(), value.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is UserState.Loading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }

                        is UserState.Success -> {
                            binding.tvNameUser.text = value.user.name
                            binding.tvEmail.text = value.user.email
                            binding.tvPhone.text = value.user.phone

                            if (value.user.photoUrl != null) {
                                Glide.with(requireContext())
                                    .load(value.user.photoUrl)
                                    .circleCrop()
                                    .into(binding.ivProfile)
                            } else {
                                binding.ivProfile.setImageResource(R.drawable.ic_default_profile)
                            }
                        }
                    }
                }

            })
        }
    }

    private fun dialogBoxConfirmationLogout() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Are you sure want to logout?")

        alertDialog.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
            viewModel.logout()
            dialog.dismiss()
            logout()
        }

        alertDialog.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun logout() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
