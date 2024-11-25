package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.content.Intent
import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityCheckoutBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.CustomLoadingDialogBinding
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
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        cartRecyclerView()
        viewModel.loadCart()
        observeCartState()
        loadingDialogPayment()
        createOrderState()

        binding.btnPayment.setOnClickListener {
            loadingDialog.show()
            viewModel.createOrderFromCart()
        }

    }

    private fun createOrderState() {
        lifecycleScope.launch {
            viewModel.orderState.collect(object : FlowCollector<OrderState> {
                override suspend fun emit(value: OrderState) {
                    when (value) {
                        is OrderState.Error -> {
                            loadingDialog.dismiss()
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
                            loadingDialog.dismiss()
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Order Created Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is OrderState.SuccessPayment -> {
                            loadingDialog.dismiss()
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

    private fun observeCartState() {
        lifecycleScope.launch {
            viewModel.cartState.collect(object : FlowCollector<CartState> {
                override suspend fun emit(value: CartState) {
                    when (value) {
                        is CartState.Loading -> {
                            Toast.makeText(this@CheckoutActivity, "Loading", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is CartState.Success -> {
                            adapter.updateData(value.carts)
                            val formattedTotal =
                                NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                                    .format(value.totalPrice)
                            binding.tvNumberTotalPrice.text = formattedTotal
                            binding.tvNumberPriceDetails.text = formattedTotal

                        }

                        is CartState.Error -> {
                            Toast.makeText(
                                this@CheckoutActivity,
                                "Failed to load data, please check your internet connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

            })
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarCheckout)
        supportActionBar.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)
            supportActionBar?.title = "Checkout"
        }
    }

    private fun cartRecyclerView() {
        adapter = ItemCartsAdapter(emptyList(), this@CheckoutActivity, showDelete = false)
        binding.rvCart.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCart.adapter = adapter
    }

    private fun loadingDialogPayment() {
        val loadingDialogBinding = CustomLoadingDialogBinding.inflate(layoutInflater)
        loadingDialog = AlertDialog.Builder(this)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
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
}
