package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseAddressViewModel @Inject constructor(
    application: Application,
    private val db: StomazonDatabase,
) : AndroidViewModel(application) {

    private val _addressList = MutableLiveData<List<AddressEntity>>()
    val addressList: LiveData<List<AddressEntity>> get() = _addressList

    init {
        loadAddress()
    }

    fun loadAddress() {
        viewModelScope.launch {
            val addresses = db.addressDao().getAddress()
            _addressList.postValue(addresses)
        }
    }

    fun deleteAddress(address: AddressEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                db.addressDao().deleteAddress(address)
                loadAddress()
                onSuccess()
            } catch (e: Exception) {
                // Menangani error jika ada
            }
        }
    }

    }
