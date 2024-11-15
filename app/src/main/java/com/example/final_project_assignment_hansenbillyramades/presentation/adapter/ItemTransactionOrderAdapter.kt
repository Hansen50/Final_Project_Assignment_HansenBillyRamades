package com.example.final_project_assignment_hansenbillyramades.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemTransactionBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrder
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemProductsAdapter.MyViewHolder
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemTransactionOrderListener
import java.text.NumberFormat
import java.util.Locale

class ItemTransactionOrderAdapter(
    private var listTransactionOrder: List<TransactionOrder>,
    private val listener: ItemTransactionOrderListener

) : RecyclerView.Adapter<ItemTransactionOrderAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listTransactionOrder.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transactionOrder = listTransactionOrder[position]

        val formattedPrice =
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(transactionOrder.totalPrice)

        holder.binding.tvIdOrder.text = transactionOrder.id
        holder.binding.tvStatus.text = transactionOrder.status.replaceFirstChar { it.uppercase() }
        holder.binding.tvNumberTotalPriceTransaction.text = formattedPrice


        holder.binding.root.setOnClickListener {
            listTransactionOrder[position].id.let { idTransactionOrder -> listener.onClick(idTransactionOrder) }
        }
    }

    fun submitList(newList: List<TransactionOrder>) {
        listTransactionOrder = newList
        notifyDataSetChanged()
    }
}


