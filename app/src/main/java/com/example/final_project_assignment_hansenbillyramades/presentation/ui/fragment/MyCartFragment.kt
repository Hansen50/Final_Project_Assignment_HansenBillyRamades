package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.Intent
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentHomeBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyCartBinding
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
    private lateinit var adapter: ItemCartsAdapter

    private val viewModel: MyCartViewModel by viewModels()

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
        observeTotalPrice()
        viewModel.loadCart()

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemCartsAdapter(emptyList(), this, showDelete = true)
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter
    }

    private fun observeCart() {
        viewModel.cartList.observe(viewLifecycleOwner) { cart ->
            adapter.updateData(cart)
        }
    }

    private fun observeTotalPrice() {
        viewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            val formattedTotal = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)
            binding.tvNumberTotalPrice.text = formattedTotal
        }
    }

    override fun onDelete(cart: CartEntity) {
        lifecycleScope.launch {
            viewModel.deleteCart(cart)
        }
    }

    override fun onUpdateQuantity(cart: CartEntity) {
        lifecycleScope.launch {
            viewModel.updateCart(cart) {
                //TODO
            }
        }
    }

    override fun onIncrement(cart: CartEntity) {
        cart.productQuantity++
        lifecycleScope.launch {
            viewModel.updateCart(cart) {
                viewModel.calculateTotalPrice(viewModel.cartList.value ?: emptyList())
            }
        }
    }

    override fun onDecrement(cart: CartEntity) {
        if (cart.productQuantity > 1) {
            cart.productQuantity--
            lifecycleScope.launch {
                viewModel.updateCart(cart) {
                    viewModel.calculateTotalPrice(viewModel.cartList.value ?: emptyList())
                }
            }
        }
    }
}

