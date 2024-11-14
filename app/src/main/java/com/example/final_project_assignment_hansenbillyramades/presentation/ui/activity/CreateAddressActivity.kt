package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityAddressDetailsBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Address
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.CreateAddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressDetailsBinding
    private val viewModel: CreateAddressViewModel by viewModels()
    private var addressToEdit: AddressEntity? = null // Store the address to be edited

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddressDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        supportActionBar?.title = "Address Detail"

        addressToEdit = intent.getParcelableExtra("addressToEdit")
        addressToEdit?.let {
            binding.etFullName.setText(it.addressRecipientName)
            binding.etPhoneNumber.setText(it.addressRecipientPhone)
            binding.etFullAddress.setText(it.addressRecipientFullAddress)
            binding.etPostCode.setText(it.addressRecipientZipPostalCode)
        }

        binding.btnCreate.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val phoneNumber = binding.etPhoneNumber.text.toString().trim()
            val fullAddress = binding.etFullAddress.text.toString().trim()
            val zipCode = binding.etPostCode.text.toString().trim()

            if (fullName.isEmpty() || phoneNumber.isEmpty() || fullAddress.isEmpty() || zipCode.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val address = Address(
                    id = addressToEdit?.id ?: 0, // If address exists, use the ID (for updating)
                    recipientName = fullName,
                    recipientPhone = phoneNumber,
                    recipientFullAddress = fullAddress,
                    recipientZipPostalCode = zipCode
                )

                if (address.id == 0) {
                    viewModel.addAddress(address) {
                        Toast.makeText(this, "Address added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    viewModel.updateAddress(address) {
                        Toast.makeText(this, "Address updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
