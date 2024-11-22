package com.example.final_project_assignment_hansenbillyramades.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemProductBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemProductListener
import java.text.NumberFormat
import java.util.Locale

class ItemProductsAdapter(
    private var listProduct: List<Products>,
    private val listener: ItemProductListener,

    ) : RecyclerView.Adapter<ItemProductsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ListItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listProduct.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = listProduct[position]

        val formattedPrice =
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(product.price)
        holder.binding.tvNameProduct.text = product.name
        holder.binding.tvPrice.text = formattedPrice
        holder.binding.tvRating.setText(String.format("%.2f", product.averageRating));


        Glide.with(holder.itemView.context)
            .load(product.image)
//            .placeholder(R.drawable.placeholder_image)
            .into(holder.binding.ivProduct)

        holder.binding.root.setOnClickListener {
            listProduct[position].id.let { idProduct -> listener.onClick(idProduct) }
        }

    }


    fun updateData(newProducts: List<Products>) {
        listProduct = newProducts
        notifyDataSetChanged()
    }
}