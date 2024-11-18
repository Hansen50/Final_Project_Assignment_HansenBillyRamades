package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityCheckoutBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.CartState
import com.example.final_project_assignment_hansenbillyramades.domain.model.OrderState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemCartsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemCartListener
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.CheckoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity(), ItemCartListener {
    private lateinit var binding: ActivityCheckoutBinding
    private val viewModel: CheckoutViewModel by viewModels()
    private lateinit var adapter: ItemCartsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCheckout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
        supportActionBar?.title = "Checkout"

        cartRecyclerView()
        viewModel.loadCart()
        observeTotalPrice()

        lifecycleScope.launch {
            viewModel.cartState.collect { cartState ->
                when (cartState) {
                    is CartState.Loading -> {
                        //TODO
                    }

                    is CartState.Success -> {
                        adapter.updateData(cartState.carts)
                    }

                    is CartState.Error -> {
                        Toast.makeText(
                            this@CheckoutActivity,
                            "Error: ${cartState.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.cardMenuAddress.setOnClickListener {
            val intent = Intent(this@CheckoutActivity, ChooseAddressActivity::class.java)
            startActivity(intent)
        }

        binding.btnPayment.setOnClickListener {
            viewModel.createOrderFromCart()
        }

        lifecycleScope.launch {
            viewModel.orderState.collect(object : FlowCollector<OrderState> {
                override suspend fun emit(value: OrderState) {
                    when (value) {
                        is OrderState.Error -> {
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Error: ${value.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        OrderState.Loading -> {
                            Toast.makeText(this@CheckoutActivity, "Loading", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is OrderState.Success -> {
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Order Created Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is OrderState.SuccessPayment -> {
                            val paymentUrl = value.paymentUrl
                            val intent = Intent(
                                this@CheckoutActivity,
                                ChoosePaymentWebViewActivity::class.java
                            )
                            intent.putExtra("paymentUrl", paymentUrl)
                            startActivity(intent)
                        }

                    }
                }
            })
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


    private fun cartRecyclerView() {
        adapter = ItemCartsAdapter(emptyList(), this@CheckoutActivity, showDelete = false)
        binding.rvCart.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCart.adapter = adapter
    }

    private fun observeTotalPrice() {
        viewModel.totalPrice.observe(this) { total ->
            val formattedTotal = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)
            binding.tvNumberTotalPrice.text = formattedTotal
            binding.tvNumberPriceDetails.text = formattedTotal
        }
    }

    override fun onDelete(cart: Cart) {
        lifecycleScope.launch {
            viewModel.deleteCart(cart)
        }
    }

    override fun onUpdateQuantity(cart: Cart) {
        lifecycleScope.launch {
            viewModel.updateCart(cart)
        }
    }

    override fun onIncrement(cart: Cart) {
        cart.quantity++
        lifecycleScope.launch {
            viewModel.updateCart(cart)
        }
    }

    override fun onDecrement(cart: Cart) {
        if (cart.quantity > 1) {
            cart.quantity--
            lifecycleScope.launch {
                viewModel.updateCart(cart)
            }
        }
    }
}
