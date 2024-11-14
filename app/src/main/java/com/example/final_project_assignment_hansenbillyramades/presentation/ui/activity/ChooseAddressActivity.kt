package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityChooseAddressBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Address
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemAddressAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemAddressListener
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.ChooseAddressViewModel
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyCartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class ChooseAddressActivity : AppCompatActivity(), ItemAddressListener {
    private lateinit var binding: ActivityChooseAddressBinding
    private lateinit var adapter: ItemAddressAdapter
    private val viewModel: ChooseAddressViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadAddress()
        cartRecyclerView()
        observeAddress()

        setSupportActionBar(binding.toolbarChooseAddress)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        supportActionBar?.title = "Choose Address"

        binding.btnAddAddress.setOnClickListener {
            val intent = Intent(this@ChooseAddressActivity, CreateAddressActivity::class.java)
            startActivity(intent)
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

    private fun cartRecyclerView() {
        adapter = ItemAddressAdapter(emptyList(), this)
        binding.rvAddress.layoutManager = LinearLayoutManager(this@ChooseAddressActivity)
        binding.rvAddress.adapter = adapter
    }

    private fun observeAddress() {
        viewModel.addressList.observe(this) { addressList ->
            adapter.updateData(addressList)
        }
    }

    override fun onDelete(address: AddressEntity) {
        viewModel.deleteAddress(address) {
            Toast.makeText(this, "Address deleted successfully", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onEdit(address: AddressEntity) {
        val intent = Intent(this@ChooseAddressActivity, CreateAddressActivity::class.java)
        intent.putExtra("addressToEdit", address) // Pass the selected address to be edited
        startActivity(intent)
    }


}

