package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

        val productId = intent.getIntExtra("id_product", 0)
        Log.d("ProductDetailActivity", "Product ID: $productId")

        binding.ivShare.setOnClickListener {
            viewModel.productState.value.let { state ->
                if (state is ProductsState.SuccessDetail) {
                    val product = state.product

                    // Data yang akan dibagikan
                    val shareContent = """
                🌟 Cek produk ini!
                🛒 Nama: ${product.name}
                💸 Harga: ${formatPrice(product.price)}
                📄 Deskripsi: ${product.description}
                   Gambar: ${product.image}
                🔗 Lihat produk lengkap di aplikasi Stomazon!
            """.trimIndent()

                    // Intent untuk berbagi
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Produk dari Stomazon")
                        putExtra(Intent.EXTRA_TEXT, shareContent)
                    }

                    // Memulai intent berbagi
                    startActivity(Intent.createChooser(shareIntent, "Bagikan produk menggunakan"))
                } else {
                    Toast.makeText(this, "Data produk belum tersedia untuk dibagikan", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.ivCart.setOnClickListener {
            Log.d("ProductDetailActivity", "ivCart clicked")
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
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.isVisible = true
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Failed load data, please check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ProductsState.Loading -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.isVisible = true
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

                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.isVisible = false
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
                    viewModel.addCart(cart)
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Success added to cart",
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


