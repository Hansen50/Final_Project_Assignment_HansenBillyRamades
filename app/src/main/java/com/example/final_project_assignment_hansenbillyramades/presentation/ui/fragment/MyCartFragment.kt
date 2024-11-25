package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.Intent
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyCartBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.CartState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemCartsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemCartListener
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.CheckoutActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MyCartFragment : Fragment(), ItemCartListener {
    private var _binding: FragmentMyCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyCartViewModel by viewModels()
    private lateinit var adapter: ItemCartsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeCart()

        binding.btnCheckout.setOnClickListener {
            handleCheckout()
        }

    }

    private fun observeCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartState.collect(object : FlowCollector<CartState> {
                override suspend fun emit(value: CartState) {
                    when (value) {
                        is CartState.Loading -> {
                            binding.rvCart.isVisible = false
                        }

                        is CartState.Success -> {
                            val carts = value.carts
                            if (carts.isEmpty()) {
                                binding.rvCart.isVisible = false
                                binding.ivNoCart.isVisible = true
                                binding.tvNoCart.isVisible = true
                                binding.tvNoCartDetail.isVisible = true
                                binding.bottomButtons.isVisible = false
                            } else {
                                binding.rvCart.isVisible = true
                                binding.ivNoCart.isVisible = false
                                binding.tvNoCart.isVisible = false
                                binding.tvNoCartDetail.isVisible = false
                                binding.bottomButtons.isVisible = true
                            }
                            adapter.updateData(carts)
                            val formattedTotal =
                                NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                                    .format(value.totalPrice)
                            binding.tvNumberTotalPrice.text = formattedTotal
                        }

                        is CartState.Error -> {
                            binding.rvCart.isVisible = false
                            Toast.makeText(requireContext(), value.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }

            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCart()
    }


    private fun setupRecyclerView() {
        adapter = ItemCartsAdapter(emptyList(), this, showDelete = true)
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter
    }


    private fun handleCheckout() {
        val intent = Intent(requireContext(), CheckoutActivity::class.java)
        startActivity(intent)
    }

    override fun onDelete(cart: Cart) {
        viewModel.deleteCart(cart)
    }


    override fun onIncrement(cart: Cart) {
        cart.quantity++
        viewModel.updateCart(cart)
    }

    override fun onDecrement(cart: Cart) {
        if (cart.quantity > 1) {
            cart.quantity--
            viewModel.updateCart(cart)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


