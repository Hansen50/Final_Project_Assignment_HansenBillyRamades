package com.example.final_project_assignment_hansenbillyramades.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemTransactionBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrder
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemTransactionOrderListener
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ItemTransactionOrderAdapter(
    private var listTransactionOrder: List<TransactionOrder>,
    private val listener: ItemTransactionOrderListener,

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

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transactionOrder = listTransactionOrder[position]

        val formattedPrice =
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(transactionOrder.totalPrice)
        val formattedDate = LocalDate.parse(transactionOrder.dateOrder.substring(0, 10))
            .format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("en", "ID")))

        holder.binding.tvIdOrder.text = transactionOrder.id
        holder.binding.tvStatus.text = transactionOrder.status.replaceFirstChar { it.uppercase() }
        holder.binding.tvNumberTotalPriceTransaction.text = formattedPrice
        holder.binding.tvDate.text = formattedDate


        holder.binding.root.setOnClickListener {
            listener.onClick(listTransactionOrder[position].id)
        }

    }

    fun updateData(newList: List<TransactionOrder>) {
        listTransactionOrder = newList
        notifyDataSetChanged()
    }
}


