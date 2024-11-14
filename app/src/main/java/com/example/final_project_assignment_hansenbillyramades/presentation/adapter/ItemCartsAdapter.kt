package com.example.final_project_assignment_hansenbillyramades.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemCartBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemProductBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemCartListener
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemProductListener
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyCartViewModel
import java.text.NumberFormat
import java.util.Locale

class ItemCartsAdapter(
    private var listCart: List<CartEntity>,
    private val listener: ItemCartListener,
    private val showDelete: Boolean,
) : RecyclerView.Adapter<ItemCartsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ListItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listCart.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cart = listCart[position]
        holder.binding.tvTitleProduct.text = cart.productName
        val formattedPrice =
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(cart.productPrice)
        holder.binding.tvPrice.text = formattedPrice
        holder.binding.etQuantity.setText(cart.productQuantity.toString())

        Glide.with(holder.itemView.context)
            .load(cart.image)
            .into(holder.binding.ivProduct)

        holder.binding.ivDeleteCart.visibility = if (showDelete) View.VISIBLE else View.GONE

        holder.binding.ivDeleteCart.setOnClickListener {
            listener.onDelete(cart)
        }

        holder.binding.btnIncreament.setOnClickListener {
            listener.onIncrement(cart)
        }

        holder.binding.btnDecreament.setOnClickListener {
            listener.onDecrement(cart)
        }
    }


    fun updateData(newCarts: List<CartEntity>) {
        listCart = newCarts
        notifyDataSetChanged()
    }
}
