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
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductsActivity : AppCompatActivity(), ItemProductListener {
    private lateinit var binding: ActivitySearchProductsBinding
    private val viewModel: SearchProductsViewModel by viewModels()
    private lateinit var adapter: ItemProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupRecyclerView()
        setupSearchView()
        observeProductState()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSearchProductAll)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemProductsAdapter(emptyList(), this)
        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.adapter = adapter
        viewModel.loadAllProducts("", null)
    }

    private fun setupSearchView() {
        binding.svSearchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.loadAllProducts(query, null)
                }
                binding.svSearchProduct.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.loadAllProducts(null, null)
                }
                return true
            }
        })
    }

    private fun observeProductState() {
        lifecycleScope.launch {
            viewModel.productsState.collect(object : FlowCollector<ProductsState> {
                override suspend fun emit(value: ProductsState) {
                    when (value) {
                        is ProductsState.Error -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.isVisible = true
                            binding.rvProducts.isVisible = false
                            showToast(value.message)
                        }
                        is ProductsState.Loading -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.isVisible = true
                            binding.rvProducts.isVisible = false
                        }
                        is ProductsState.Success -> {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.rvProducts.isVisible = true
                            Log.d("SearchProductsActivity", "Received ${value.products.size} products")
                            if (value.products.isNotEmpty()) {
                                adapter.updateData(value.products)
                            } else {
                                showToast("No products found")
                            }
                        }

                        else -> {}

                    }
                }

            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("id_product", id)
        startActivity(intent)
    }
}