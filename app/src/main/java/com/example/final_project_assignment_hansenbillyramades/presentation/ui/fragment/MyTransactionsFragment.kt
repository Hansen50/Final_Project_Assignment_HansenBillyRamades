package com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyCartBinding
import com.example.final_project_assignment_hansenbillyramades.databinding.FragmentMyTransactionsBinding
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderState
import com.example.final_project_assignment_hansenbillyramades.presentation.adapter.ItemTransactionOrderAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.viewModel.MyTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTransactionsFragment : Fragment() {
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
            val adapter = ItemTransactionOrderAdapter(emptyList())
            binding.rvMyTransaction.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMyTransaction.adapter = adapter
            viewModel.loadAllTransactionOrder()


            lifecycleScope.launch {
                viewModel.transactionOrderState.collect(object :
                    FlowCollector<TransactionOrderState> {
                    override suspend fun emit(value: TransactionOrderState) {
                        when(value) {
                            is TransactionOrderState.Error -> {
                                //TODO
                            }
                            is TransactionOrderState.Loading -> {
                                //TODO
                            }
                            is TransactionOrderState.Success -> {
                                adapter.submitList(value.transactionOrder)
                            }
                        }
                    }
                })
            }
        }
    }
}