package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentHomeBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.UserState
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
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
    private var isLoading = false // untuk mencegah pemanggilan API berulang kali


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserInfo()

        binding.searchBarProduct.setOnClickListener {
            val intent = Intent(requireContext(), SearchProductsActivity::class.java)
            startActivity(intent)
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

        lifecycleScope.launch {
            viewModel.userState.collect(object : FlowCollector<UserState> {
                override suspend fun emit(value: UserState) {
                    when (value) {
                        is UserState.Error -> {
                            Toast.makeText(requireContext(), value.message, Toast.LENGTH_SHORT).show()
                        }
                        is UserState.Loading -> {
                            Toast.makeText(requireContext(), "Failed to load user data, please check your internet connection", Toast.LENGTH_SHORT).show()
                        }
                        is UserState.Success -> {
                            binding.tvUserName.text = value.user.name
                        }
                    }
                }

            })
        }

        lifecycleScope.launch {
            val adapter = ItemProductsAdapter(mutableListOf(), this@HomeFragment)
            binding.rvProducts.adapter = adapter
            binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
            viewModel.loadAllProducts(null, null)

            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadAllProducts(null, null)
                binding.swipeRefreshLayout.isRefreshing = false
            }

            binding.rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + 5)) {
                        isLoading = true
                        viewModel.loadAllProducts(null, null)
                    }
                }
            })


            lifecycleScope.launch {
                viewModel.productsState.collect(object : FlowCollector<ProductsState> {
                    override suspend fun emit(value: ProductsState) {
                        when (value) {
                            is ProductsState.Error -> {
                                binding.shimmerLayout.startShimmer()
                                binding.shimmerLayout.isVisible = true
                                binding.rvProducts.isVisible = false
                                binding.swipeRefreshLayout.isRefreshing = false
                                Toast.makeText(requireContext(), "Failed to load data product, please check your internet connection", Toast.LENGTH_SHORT)
                                    .show()
                                isLoading = false
                            }

                            is ProductsState.Loading -> {
                                binding.shimmerLayout.startShimmer()
                                binding.shimmerLayout.isVisible = true
                                binding.rvProducts.isVisible = false
                                binding.swipeRefreshLayout.isRefreshing = true
                            }

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
                                isLoading = false

                            }

                            else -> {}
                        }
                    }

                })
            }
        }
    }

    override fun onClick(id: Int) {
        val intent = Intent(requireContext(), ProductDetailActivity::class.java)
        intent.putExtra("id_product", id)
        startActivity(intent)
    }

    private fun navigateToCategoryActivity(categoryName: String?) {
        val intent = Intent(requireContext(), CategoryProductActivity::class.java)
        intent.putExtra("CATEGORY_NAME", categoryName)
        startActivity(intent)
    }
}

