package com.nocturne.store.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nocturne.store.core.ui.Resource
import com.nocturne.store.data.remote.model.ProductsResponse
import com.nocturne.store.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<Resource<ProductsResponse>?>(null)
    val results: StateFlow<Resource<ProductsResponse>?> = _results.asStateFlow()

    init {
        _query
            .debounce(400)
            .distinctUntilChanged()
            .filter { it.length >= 2 }
            .flatMapLatest { q -> repository.searchProducts(q) }
            .onEach { _results.value = it }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(q: String) {
        _query.value = q
        if (q.length < 2) _results.value = null
    }
}
