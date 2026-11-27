package com.nocturne.store.data.remote.api

import com.nocturne.store.data.remote.model.CategoryItem
import com.nocturne.store.data.remote.model.Product
import com.nocturne.store.data.remote.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit")  limit: Int  = 20,
        @Query("skip")   skip: Int   = 0,
        @Query("select") select: String = "id,title,price,discountPercentage,rating,thumbnail,category,stock,brand,description,images"
    ): ProductsResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q")      query: String,
        @Query("limit")  limit: Int = 20,
        @Query("skip")   skip: Int  = 0
    ): ProductsResponse

    @GET("products/categories")
    suspend fun getCategories(): List<CategoryItem>

    @GET("products/category/{slug}")
    suspend fun getProductsByCategory(
        @Path("slug")    slug: String,
        @Query("limit")  limit: Int = 20,
        @Query("skip")   skip: Int  = 0
    ): ProductsResponse
}
