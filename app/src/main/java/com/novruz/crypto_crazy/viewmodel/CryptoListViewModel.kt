package com.novruz.crypto_crazy.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novruz.crypto_crazy.model.CryptoListItem
import com.novruz.crypto_crazy.repository.CryptoRepository
import com.novruz.crypto_crazy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepository
) : ViewModel() {

    var cryptoList = mutableStateOf<List<CryptoListItem>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun loadCryptos() {
        viewModelScope.launch {
            isLoading.value = true

            val result = repository.getCryptoList()

            when(result) {
                is Resource.Error -> {
                    isLoading.value = false
                    errorMessage.value = result.message!!
                }

                is Resource.Success -> {
                    isLoading.value = false

                    val cryptoItems = result.data!!.mapIndexed { index, cryptoListItem ->
                        CryptoListItem(cryptoListItem.currency, cryptoListItem.price)

                    } as List<CryptoListItem>

                    errorMessage.value = ""
                    cryptoList.value += cryptoItems
                }
            }
        }

    }
}