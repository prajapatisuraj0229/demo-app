package com.nocturne.store.domain.model

/**
 * Domain-level cart item, decoupled from the API Product model.
 */
data class CartItem(
    val productId: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    var quantity: Int = 1
) {
    val lineTotal: Double get() = price * quantity
}
