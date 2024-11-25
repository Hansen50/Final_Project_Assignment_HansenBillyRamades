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
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityProductDetailBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ImagePagerAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.ProductDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

        setupActionBar()

        val productId = intent.getIntExtra("id_product", 0)
        Log.d("ProductDetailActivity", "Product ID: $productId")

        observeProductDetails(productId)

        binding.ivShare.setOnClickListener {
            shareProduct()
        }

        binding.btnCart.setOnClickListener {
            addToCart()
        }


        binding.ivCart.setOnClickListener {
            Log.d("ProductDetailActivity", "ivCart clicked")
            val intent = Intent(this@ProductDetailActivity, MainActivity::class.java)
            intent.putExtra("navigateTo", "MyCartFragment")
            startActivity(intent)
            finish()
        }

    }

    private fun shareProduct() {
        lifecycleScope.launch {
            viewModel.productState.collect(object : FlowCollector<ProductsState> {
                override suspend fun emit(value: ProductsState) {
                    when (value) {
                        is ProductsState.SuccessDetail -> {
                            val product = value.product

                            val shareContent = """
                            Check Product!
                            Name: ${product.name}
                            Price: ${formatPrice(product.price)}
                            Description: ${product.description}
                            Check out the product in Stomazon App!
                        """.trimIndent()

                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Product from Stomazon")
                                putExtra(Intent.EXTRA_TEXT, shareContent)
                            }

                            startActivity(Intent.createChooser(shareIntent, "Share Product With"))
                        }

                        is ProductsState.Error -> {
                            Toast.makeText(
                                this@ProductDetailActivity,
                                "Product data is not yet available to share",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@ProductDetailActivity,
                                "Loading product details, please wait...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
    }


    private fun addToCart() {
        lifecycleScope.launch {
            viewModel.productState.collect { state ->
                when (state) {
                    is ProductsState.SuccessDetail -> {
                        val product = state.product
                        val firstImage = product.images.firstOrNull() ?: ""
                        val cart = Cart(
                            id = product.id,
                            cartName = product.name,
                            cartPrice = product.price,
                            quantity = 1,
                            cartImage = firstImage
                        )
                        viewModel.addCart(cart)
                    }

                    is ProductsState.AddedToCart -> {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Product added to cart successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@ProductDetailActivity, MainActivity::class.java)
                        intent.putExtra("navigateTo", "MyCartFragment")
                        startActivity(intent)
                        finish()
                    }

                    is ProductsState.Error -> {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Error: ${state.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Please wait, loading product...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun observeProductDetails(productId: Int) {
        lifecycleScope.launch {
            viewModel.getProductDetail(productId)
            viewModel.productState.collect(object : FlowCollector<ProductsState> {
                override suspend fun emit(value: ProductsState) {
                    when (value) {
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
                            val product = value.product
                            binding.tvTitleProduct.text = product.name
                            binding.tvCurrency.text = formatPrice(product.price)
                            binding.tvContentDescription.text = product.description
                            binding.tvTypeCategory.text = product.category
                            binding.tvTotalQuantity.text = "${product.quantity}"
                            binding.tvRatingAverage.setText(String.format("%.2f",product.averageRating)
                            )
                            val imageUrls = product.images
                            setupViewPager(imageUrls)

//                            Glide.with(this@ProductDetailActivity)
//                                .load(product.image)
//                                .into(binding.ivProduct)

                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                        }

                        else -> {
                        }

                    }
                }

            })
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarProductDetail)
        supportActionBar.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        }
    }

    private fun setupViewPager(images: List<String>) {
        val adapter = ImagePagerAdapter(images)
        binding.viewPagerImages.adapter = adapter
        val tabLayoutMediator = TabLayoutMediator(
            binding.tabLayoutIndicator,
            binding.viewPagerImages, object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    //TODO
                }
            }
        )
        tabLayoutMediator.attach()
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


