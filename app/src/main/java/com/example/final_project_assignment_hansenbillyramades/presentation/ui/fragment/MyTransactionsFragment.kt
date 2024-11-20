package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.Intent
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
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyTransactionsBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemCartsAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemTransactionOrderAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemTransactionOrderListener
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.TransactionOrderDetailActivity
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTransactionsFragment : Fragment(), ItemTransactionOrderListener {
    private var _binding: FragmentMyTransactionsBinding? = null
    private val viewModel: MyTransactionViewModel by viewModels()
    private val binding get() = _binding!!
    private lateinit var adapter: ItemTransactionOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeTransactionOrderData()

        }

    private fun observeTransactionOrderData() {
        lifecycleScope.launch {
            viewModel.transactionOrderState.collect(object :
                FlowCollector<TransactionOrderState> {
                override suspend fun emit(value: TransactionOrderState) {
                    when (value) {
                        is TransactionOrderState.Error -> {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.rvMyTransaction.isVisible = false
                            Toast.makeText(requireContext(), value.message, Toast.LENGTH_SHORT).show()
                        }
                        is TransactionOrderState.Loading -> {
                            binding.shimmerLayout.startShimmer()
                            binding.shimmerLayout.isVisible = true
                            binding.rvMyTransaction.isVisible = false
                        }
                        is TransactionOrderState.Success -> {
                            val orders = value.transactionOrder
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            if (orders.isEmpty()) {
                                binding.shimmerLayout.stopShimmer()
                                binding.shimmerLayout.isVisible = false
                                binding.ivNoOrder.isVisible = true
                                binding.tvNoOrder.isVisible = true
                                binding.tvNoOrderDetail.isVisible = true
                            } else {
                                binding.ivNoOrder.isVisible = false
                                binding.tvNoOrder.isVisible = false
                                binding.tvNoOrderDetail.isVisible = false
                                binding.rvMyTransaction.isVisible = true
                                adapter.updateData(value.transactionOrder)
                            }
                        }

                        else -> {}
                    }
                }
            })
        }
    }


    private fun setupRecyclerView() {
        adapter = ItemTransactionOrderAdapter(emptyList(), this@MyTransactionsFragment)
        binding.rvMyTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMyTransaction.adapter = adapter
        viewModel.loadAllTransactionOrder()
    }

    override fun onClick(id: String) {
        val intent = Intent(requireContext(), TransactionOrderDetailActivity::class.java)
        intent.putExtra("id_transaction_order", id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}