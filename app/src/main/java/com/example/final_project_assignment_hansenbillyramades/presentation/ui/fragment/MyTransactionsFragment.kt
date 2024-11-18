package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyCartBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyTransactionsBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemTransactionOrderAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.listener.ItemTransactionOrderListener
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity.ProductDetailActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val adapter = ItemTransactionOrderAdapter(emptyList(), this@MyTransactionsFragment)
            binding.rvMyTransaction.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMyTransaction.adapter = adapter
            viewModel.loadAllTransactionOrder()


            lifecycleScope.launch {
                viewModel.transactionOrderState.collect(object :
                    FlowCollector<TransactionOrderState> {
                    override suspend fun emit(value: TransactionOrderState) {
                        when(value) {
                            is TransactionOrderState.Error -> {
                                binding.shimmerLayout.startShimmer()
                                binding.shimmerLayout.isVisible = true
                                binding.rvMyTransaction.isVisible = false
                            }
                            is TransactionOrderState.Loading -> {
                                binding.shimmerLayout.startShimmer()
                                binding.shimmerLayout.isVisible = true
                                binding.rvMyTransaction.isVisible = false
                            }
                            is TransactionOrderState.Success -> {
                                binding.shimmerLayout.stopShimmer()
                                binding.shimmerLayout.isVisible = false
                                binding.rvMyTransaction.isVisible = true
                                adapter.submitList(value.transactionOrder)
                            }

                            else -> {}
                        }
                    }
                })
            }
        }
    }

    override fun onClick(id: String) {
        val intent = Intent(requireContext(), TransactionOrderDetailActivity::class.java)
        intent.putExtra("id_transaction_order", id)
        startActivity(intent)
    }
}