package com.nocturne.store.domain.repository

import com.nocturne.store.domain.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-memory singleton cart repository.
 * Backed by StateFlow so all observers react to changes.
 */
object CartRepository {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val totalCount: Int get() = _items.value.sumOf { it.quantity }
    val totalPrice: Double get() = _items.value.sumOf { it.lineTotal }

    fun addItem(item: CartItem) {
        _items.update { current ->
            val existing = current.find { it.productId == item.productId }
            if (existing != null) {
                current.map {
                    if (it.productId == item.productId) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                current + item
            }
        }
    }

    fun removeItem(productId: Int) {
        _items.update { it.filter { item -> item.productId != productId } }
    }

    fun incrementQuantity(productId: Int) {
        _items.update { current ->
            current.map { if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it }
        }
    }

    fun decrementQuantity(productId: Int) {
        _items.update { current ->
            current.mapNotNull { item ->
                if (item.productId == productId) {
                    if (item.quantity > 1) item.copy(quantity = item.quantity - 1) else null
                } else item
            }
        }
    }

    fun clearCart() {
        _items.update { emptyList() }
    }
}
