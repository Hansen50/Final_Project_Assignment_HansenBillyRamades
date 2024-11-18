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
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentHomeBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyCartBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.CartState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemCartsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemCartListener
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.CheckoutActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyCartViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.loadCart()
        observeTotalPrice()

        lifecycleScope.launch {
            viewModel.cartState.collect { cartState ->
                when (cartState) {
                    is CartState.Loading -> {
                        binding.rvCart.isVisible = false
                    }
                    is CartState.Success -> {
                        val carts = cartState.carts
                        adapter.updateData(carts)
                        binding.rvCart.isVisible = true
                    }
                    is CartState.Error -> {
                        binding.rvCart.isVisible = false
                        Toast.makeText(requireContext(), cartState.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnCheckout.setOnClickListener {
            handleCheckout() }
    }

    private fun setupRecyclerView() {
        adapter = ItemCartsAdapter(emptyList(), this, showDelete = true)
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter
    }

    private fun observeTotalPrice() {
        lifecycleScope.launch {
            viewModel.totalPrice.collect { total ->
                val formattedTotal =
                    NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)
                binding.tvNumberTotalPrice.text = formattedTotal
            }
        }
    }

    private fun handleCheckout() {
        val cartState = viewModel.cartState.value
        if (cartState is CartState.Success && cartState.carts.isNotEmpty()) {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDelete(cart: Cart) {
        viewModel.deleteCart(cart)
    }

    override fun onUpdateQuantity(cart: Cart) {
        viewModel.updateCart(cart)
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
}


