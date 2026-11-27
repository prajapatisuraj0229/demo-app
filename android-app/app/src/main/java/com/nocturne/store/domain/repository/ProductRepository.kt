package com.nocturne.store.domain.repository

import com.nocturne.store.core.ui.Resource
import com.nocturne.store.data.remote.RetrofitClient
import com.nocturne.store.data.remote.model.CategoryItem
import com.nocturne.store.data.remote.model.Product
import com.nocturne.store.data.remote.model.ProductsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository {

    private val api = RetrofitClient.productApi

    fun getProducts(limit: Int = 20, skip: Int = 0): Flow<Resource<ProductsResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = api.getProducts(limit = limit, skip = skip)
            if (result.products.isEmpty()) {
                emit(Resource.Empty("No products found."))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", isNetworkError = true))
        }
    }

    fun getProductById(id: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(api.getProductById(id)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", isNetworkError = true))
        }
    }

    fun searchProducts(query: String, limit: Int = 20, skip: Int = 0): Flow<Resource<ProductsResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = api.searchProducts(query, limit, skip)
            if (result.products.isEmpty()) {
                emit(Resource.Empty("No results for \"$query\"."))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", isNetworkError = true))
        }
    }

    fun getCategories(): Flow<Resource<List<CategoryItem>>> = flow {
        emit(Resource.Loading)
        try {
            val result = api.getCategories()
            if (result.isEmpty()) {
                emit(Resource.Empty("No categories found."))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", isNetworkError = true))
        }
    }

    fun getProductsByCategory(slug: String, limit: Int = 20, skip: Int = 0): Flow<Resource<ProductsResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = api.getProductsByCategory(slug, limit, skip)
            if (result.products.isEmpty()) {
                emit(Resource.Empty("No products in this category."))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", isNetworkError = true))
        }
    }
}
