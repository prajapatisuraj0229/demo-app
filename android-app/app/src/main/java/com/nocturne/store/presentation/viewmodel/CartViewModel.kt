package com.nocturne.store.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.nocturne.store.domain.model.CartItem
import com.nocturne.store.domain.repository.CartRepository
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = CartRepository.items

    val totalCount: Int get() = CartRepository.totalCount
    val totalPrice: Double get() = CartRepository.totalPrice

    fun addToCart(item: CartItem) = CartRepository.addItem(item)

    fun removeFromCart(productId: Int) = CartRepository.removeItem(productId)

    fun increment(productId: Int) = CartRepository.incrementQuantity(productId)

    fun decrement(productId: Int) = CartRepository.decrementQuantity(productId)

    fun clearCart() = CartRepository.clearCart()

    fun isInCart(productId: Int): Boolean =
        CartRepository.items.value.any { it.productId == productId }
}
