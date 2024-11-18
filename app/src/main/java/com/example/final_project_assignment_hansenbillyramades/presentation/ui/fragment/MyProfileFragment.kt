package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyProfileBinding
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.LoginActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.HomeViewModel
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
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

        viewModel.user.observe(viewLifecycleOwner) { userInfo ->
            binding.tvNameUser.text = userInfo.name
            binding.tvEmail.text = userInfo.email
            binding.tvPhone.text = userInfo.phone

            if (userInfo.photoUrl != null) {
                Glide.with(requireContext())
                    .load(userInfo.photoUrl)
                    .circleCrop()
                    .into(binding.ivProfile)
            } else {
                binding.ivProfile.setImageResource(R.drawable.ic_default_profile)
            }
        }
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
