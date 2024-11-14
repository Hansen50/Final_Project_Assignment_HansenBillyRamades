package com.example.final_project_assignment_hansenbillyramades.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemAddressBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.ListItemCartBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.Address
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemCartsAdapter.MyViewHolder
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemAddressListener

class ItemAddressAdapter(
    private var listAddress: List<AddressEntity>,
    private val listener: ItemAddressListener,
) : RecyclerView.Adapter<ItemAddressAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ListItemAddressBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ListItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listAddress.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val address = listAddress[position]

        holder.binding.tvNameRecipient.text = address.addressRecipientName
        holder.binding.tvPhoneNumberRecipient.text = address.addressRecipientPhone
        holder.binding.tvAddressRecipient.text = address.addressRecipientFullAddress

        holder.binding.ivEditAddress.setOnClickListener {
            listener.onEdit(address)
        }

        holder.binding.ivDeleteAddress.setOnClickListener {
            listener.onDelete(address)
        }
    }

    fun updateData(newAddress: List<AddressEntity>) {
        listAddress = newAddress
        notifyDataSetChanged()
    }
}
