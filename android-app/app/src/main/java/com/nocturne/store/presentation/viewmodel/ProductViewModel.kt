package com.nocturne.store.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nocturne.store.core.ui.Resource
import com.nocturne.store.data.remote.model.CategoryItem
import com.nocturne.store.data.remote.model.Product
import com.nocturne.store.data.remote.model.ProductsResponse
import com.nocturne.store.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val PAGE_SIZE = 20

    // ── Products list ──────────────────────────────────────────
    private val _products = MutableStateFlow<Resource<ProductsResponse>>(Resource.Loading)
    val products: StateFlow<Resource<ProductsResponse>> = _products.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    // ── Selected product detail ────────────────────────────────
    private val _productDetail = MutableStateFlow<Resource<Product>>(Resource.Loading)
    val productDetail: StateFlow<Resource<Product>> = _productDetail.asStateFlow()

    // ── Categories ────────────────────────────────────────────
    private val _categories = MutableStateFlow<Resource<List<CategoryItem>>>(Resource.Loading)
    val categories: StateFlow<Resource<List<CategoryItem>>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts(page: Int = 0, categorySlug: String? = _selectedCategory.value) {
        val skip = page * PAGE_SIZE
        viewModelScope.launch {
            (if (categorySlug != null) {
                repository.getProductsByCategory(categorySlug, PAGE_SIZE, skip)
            } else {
                repository.getProducts(PAGE_SIZE, skip)
            }).collect { _products.value = it }
            _currentPage.value = page
        }
    }

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            repository.getProductById(id).collect { _productDetail.value = it }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().collect { _categories.value = it }
        }
    }

    fun selectCategory(slug: String?) {
        _selectedCategory.value = slug
        loadProducts(page = 0, categorySlug = slug)
    }

    fun nextPage(total: Int) {
        val next = _currentPage.value + 1
        if (next * PAGE_SIZE < total) loadProducts(next)
    }

    fun previousPage() {
        val prev = _currentPage.value - 1
        if (prev >= 0) loadProducts(prev)
    }
}
