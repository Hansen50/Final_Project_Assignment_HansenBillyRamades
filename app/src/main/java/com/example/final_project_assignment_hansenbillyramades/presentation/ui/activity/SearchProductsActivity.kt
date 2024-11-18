package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivitySearchProductsBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemProductsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemProductListener
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.CategoryProductViewModel
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.SearchProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductsActivity : AppCompatActivity(), ItemProductListener {
    private lateinit var binding: ActivitySearchProductsBinding
    private val viewModel: SearchProductsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearchProductAll)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)


        val adapter = ItemProductsAdapter(mutableListOf(), this@SearchProductsActivity)
        binding.rvProducts.layoutManager =
            GridLayoutManager(this@SearchProductsActivity, 2)
        binding.rvProducts.adapter = adapter
        viewModel.loadAllProducts("", null)


        binding.svSearchProduct.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {
                if (!search.isNullOrEmpty()) {
                    viewModel.loadAllProducts(search, null)
                }
                binding.svSearchProduct.clearFocus()
                return true
            }

            override fun onQueryTextChange(newSearch: String?): Boolean {
                if (newSearch.isNullOrEmpty()) {
                    viewModel.loadAllProducts(null, null)
                }
                return true
            }
        })

        lifecycleScope.launch {
            viewModel.productsState.collect { value ->
                when (value) {
                    is ProductsState.Error -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.isVisible = true
                        binding.rvProducts.isVisible = false
                        Toast.makeText(
                            this@SearchProductsActivity,
                            value.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ProductsState.Loading -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.isVisible = true
                        binding.rvProducts.isVisible = false
                        Toast.makeText(this@SearchProductsActivity, "Loading", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ProductsState.Success -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.isVisible = false
                        binding.rvProducts.isVisible = true
                        Log.d(
                            "CategoryProductActivity",
                            "Received Product Category ${value.products.size} products"
                        )
                        if (value.products.isNotEmpty()) {
                            adapter.updateData(value.products)
                        } else {
                            Toast.makeText(
                                this@SearchProductsActivity,
                                "No products found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    else -> {}
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

    override fun onClick(id: Int) {
        val intent = Intent(this@SearchProductsActivity, ProductDetailActivity::class.java)
        intent.putExtra("id_product", id)
        startActivity(intent)
    }
}