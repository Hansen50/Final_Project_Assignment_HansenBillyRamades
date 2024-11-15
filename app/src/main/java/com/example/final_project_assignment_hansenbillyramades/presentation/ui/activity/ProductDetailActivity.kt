package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityProductDetailBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.MyCartFragment
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarProductDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        supportActionBar?.title = "Product Detail"

        val productId = intent.getIntExtra("id_product", 0)
        Log.d("ProductDetailActivity", "Product ID: $productId")

        binding.ivCart.setOnClickListener {
            val intent = Intent(this@ProductDetailActivity, MainActivity::class.java)
            intent.putExtra("navigateTo", "MyCartFragment")
            startActivity(intent)
            finish()
        }

        lifecycleScope.launch {
            viewModel.getProductDetail(productId)

            viewModel.productState.collect { state ->
                when (state) {
                    is ProductsState.Error -> {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Error: ${state.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ProductsState.Loading -> {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Loading...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ProductsState.SuccessDetail -> {
                        val product = state.product
                        binding.tvTitleProduct.text = product.name
                        binding.tvCurrency.text = formatPrice(product.price)
                        binding.tvContentDescription.text = product.description
                        binding.tvTypeCategory.text = product.category
                        binding.tvTotalQuantity.text = "${product.quantity}"
                        binding.tvRatingAverage.setText(String.format("%.2f", product.averageRating))

                        Glide.with(this@ProductDetailActivity)
                            .load(product.image)
                            .into(binding.ivProduct)
                    }

                    else -> {
                    }
                }
            }
        }

        binding.btnCart.setOnClickListener {
            viewModel.productState.value.let { state ->
                if (state is ProductsState.SuccessDetail) {
                    val product = state.product
                    val cart = CartEntity(
                        productId = product.id,
                        productName = product.name?: "",
                        productPrice = product.price,
                        productQuantity = 1,
                        image = product.image?: ""
                    )
                    viewModel.addCart(cart) {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "${product.name} SuccessFully Added to Cart",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@ProductDetailActivity, MainActivity::class.java)
                        intent.putExtra("navigateTo", "MyCartFragment")
                        startActivity(intent)
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

    private fun formatPrice(price: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(price)
    }
}


