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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarProductCategory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        supportActionBar?.title = categoryName

        val adapter = ItemProductsAdapter(mutableListOf(), this@CategoryProductActivity)
        binding.rvProductsCategory.layoutManager =
            GridLayoutManager(this@CategoryProductActivity, 2)
        binding.rvProductsCategory.adapter = adapter
        viewModel.getProductsByCategory(categoryName, "")

        binding.svSearchProducCategory.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {
                if (!search.isNullOrEmpty()) {
                    viewModel.getProductsByCategory(categoryName, search)
                }
                binding.svSearchProducCategory.clearFocus()
                return true
            }

            override fun onQueryTextChange(newSearch: String?): Boolean {
                if (newSearch.isNullOrEmpty()) {
                    viewModel.getProductsByCategory(categoryName, null)
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
                        binding.rvProductsCategory.isVisible = false
                        Toast.makeText(
                            this@CategoryProductActivity,
                            value.message,
                            Toast.LENGTH_SHORT
                        ).show()
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
                        Log.d(
                            "CategoryProductActivity",
                            "Received Product Category ${value.products.size} products"
                        )
                        if (value.products.isNotEmpty()) {
                            adapter.updateData(value.products)
                        } else {
                            Toast.makeText(
                                this@CategoryProductActivity,
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
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("id_product", id)
        startActivity(intent)
    }
}
