package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentHomeBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.model.UserState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemProductsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemProductListener
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.CategoryProductActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.ProductDetailActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.SearchProductsActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemProductListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: ItemProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        observeUserState()
        observeProductsState()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadAllProducts(null)
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun setupUI() {
        binding.searchBarProduct.setOnClickListener {
            startActivity(Intent(requireContext(), SearchProductsActivity::class.java))
        }

        binding.menuCardViewClothes.setOnClickListener {
            navigateToCategoryActivity("CLOTHES")
        }

        binding.menuCardViewElectronics.setOnClickListener {
            navigateToCategoryActivity("ELECTRONICS")
        }

        binding.menuCardViewFurniture.setOnClickListener {
            navigateToCategoryActivity("FURNITURE")
        }

        binding.menuCardViewShoes.setOnClickListener {
            navigateToCategoryActivity("SHOES")
        }

        binding.menuCardViewMisc.setOnClickListener {
            navigateToCategoryActivity("MISC")
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemProductsAdapter(emptyList(), this@HomeFragment)
        binding.rvProducts.adapter = adapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun observeUserState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userState.collect(object : FlowCollector<UserState> {
                override suspend fun emit(value: UserState) {
                    when (value) {
                        is UserState.Error -> {
                            showToast("Failed to load user data, please check your internet connection")
                        }

                        is UserState.Loading -> {}

                        is UserState.Success -> {
                            val firstName = value.user.name.split(" ").firstOrNull() ?: "No Name"
                            binding.tvUserName.text = firstName
                        }
                    }
                }
            })
        }
    }

    private fun observeProductsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsState.collect(object : FlowCollector<ProductsState> {
                override suspend fun emit(value: ProductsState) {
                    when (value) {
                        is ProductsState.Success -> {
                            Log.d(
                                "HomeFragmentData",
                                "ReceivedProduct ${value.products.size} products"
                            )
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.rvProducts.isVisible = true
                            binding.swipeRefreshLayout.isRefreshing = false
                            adapter.updateData(value.products)

                        }

                        is ProductsState.Loading -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.isVisible = true
                            binding.rvProducts.isVisible = false
                        }

                        is ProductsState.Error -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.isVisible = true
                            binding.rvProducts.isVisible = false
                            binding.swipeRefreshLayout.isRefreshing = false
                            showToast("Failed to load data product, please check your internet connection")
                        }

                        else -> {}
                    }
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(id: Int) {
        val intent = Intent(requireContext(), ProductDetailActivity::class.java)
        intent.putExtra("id_product", id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToCategoryActivity(categoryName: String?) {
        val intent = Intent(requireContext(), CategoryProductActivity::class.java)
        intent.putExtra("CATEGORY_NAME", categoryName)
        startActivity(intent)
    }
}

// di sini saya menggunakan viewLifecycleowenr karena mengelola view pada fragment ini
// halaman maka fragment nya destoryed dan tidak ada lagi load data
// kemudian hal ini akan mencegah IllegalStateException