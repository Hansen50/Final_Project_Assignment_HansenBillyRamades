package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityCategoryProductBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemProductsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemProductListener
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.CategoryProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryProductActivity : AppCompatActivity(), ItemProductListener {
    private lateinit var binding: ActivityCategoryProductBinding
    private val viewModel: CategoryProductViewModel by viewModels()
    private lateinit var adapter: ItemProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupRecyclerView()

        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        if (categoryName.isEmpty()) {
            showToast("No category selected")
            finish()
            return
        }
        fetchProductsByCategory(categoryName)
        setupSearchView(categoryName)
        observeProductState()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarProductCategory)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemProductsAdapter(emptyList(), this)
        binding.rvProductsCategory.layoutManager = GridLayoutManager(this, 2)
        binding.rvProductsCategory.adapter = adapter
    }

    private fun setupSearchView(categoryName: String) {
        binding.svSearchProducCategory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    fetchProductsByCategory(categoryName, query)
                }
                binding.svSearchProducCategory.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    fetchProductsByCategory(categoryName, null)
                }
                return true
            }
        })
    }

    private fun fetchProductsByCategory(categoryName: String, search: String? = "") {
        viewModel.getProductsByCategory(categoryName, search)
    }

    private fun observeProductState() {
        lifecycleScope.launch {
            viewModel.productsState.collect(object : FlowCollector<ProductsState> {
                override suspend fun emit(value: ProductsState) {
                    when (value) {
                        is ProductsState.Error -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.isVisible = true
                            binding.rvProductsCategory.isVisible = false
                            showToast(value.message)
                        }

                        is ProductsState.Loading -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.isVisible = true
                            binding.rvProductsCategory.isVisible = false
                        }

                        is ProductsState.Success -> {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.rvProductsCategory.isVisible = true

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
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra("id_product", id)
        }
        startActivity(intent)
    }
}
