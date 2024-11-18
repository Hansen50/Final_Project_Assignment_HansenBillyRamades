package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityTransactionOrderDetailBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemProductsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemProductsTransactionOrderDetailAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.ProductDetailViewModel
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.TransactionOrderDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class TransactionOrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionOrderDetailBinding
    private val viewModel: TransactionOrderDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTransactionSorderDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        supportActionBar?.title = "Order Detail"

        val adapter = ItemProductsTransactionOrderDetailAdapter(emptyList())
        binding.rvProductOrdered.adapter = adapter
        binding.rvProductOrdered.layoutManager = LinearLayoutManager(this)

        val transactionOrderId = intent.getStringExtra("id_transaction_order")

        lifecycleScope.launch {
            if (transactionOrderId != null) {
                viewModel.getTransactionOrderDetail(transactionOrderId)
            }

            viewModel.transactionOrderState.collect { state ->
                when (state) {
                    is TransactionOrderState.Error -> {
                        Toast.makeText(
                            this@TransactionOrderDetailActivity,
                            "Error: ${state.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is TransactionOrderState.Loading -> {
                        Toast.makeText(
                            this@TransactionOrderDetailActivity,
                            "Loading...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is TransactionOrderState.SuccessDetail -> {
                        val order = state.order
                        val formattedPriceToRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(order.totalPrice)
                        binding.tvOrderIdDetail.text = order.id
                        binding.tvPaymentStatus.text = order.status
                        binding.tvTotalPriceOrderNumber.text = formattedPriceToRupiah
                        adapter.updateData(order.products)
                    }

                    else -> {
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