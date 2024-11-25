//package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.final_project_assignment_hansenbillyramades.data.source.local.room.StomazonDatabase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class CreateAddressViewModel @Inject constructor(
//    private val addressUseCase: AddressUseCase,
//    private val db: StomazonDatabase
//) : ViewModel() {
//
//
//    fun addAddress(address: Address, onSuccess: () -> Unit) {
//        viewModelScope.launch {
//            try {
//                addressUseCase.addAddress(address)
//                onSuccess()
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }
//
//    fun updateAddress(address: Address, onSuccess: () -> Unit) {
//        viewModelScope.launch {
//            try {
//                addressUseCase.updateAddress(address)
//                onSuccess()
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
