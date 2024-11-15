package com.example.final_project_assignment_hansenbillyramades.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemProductBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemProductTransactionOrderBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderDetail
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemProductListener
import java.text.NumberFormat
import java.util.Locale

class ItemProductsTransactionOrderDetailAdapter(
    private var listProductTransactionOrder: List<TransactionOrderDetail.Product>,

    ) : RecyclerView.Adapter<ItemProductsTransactionOrderDetailAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ListItemProductTransactionOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListItemProductTransactionOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listProductTransactionOrder.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = listProductTransactionOrder[position]
        val formattedPrice =
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(order.price)

        holder.binding.tvTitleProduct.text = order.name
        holder.binding.tvPrice.text = formattedPrice
        holder.binding.tvQuantityTransactionDetailNumber.text = "${order.quantity}"
    }


    fun updateData(newListProductTransactionOrder: List<TransactionOrderDetail.Product>) {
        listProductTransactionOrder = newListProductTransactionOrder
        notifyDataSetChanged()
    }

}


