package com.nocturne.store.presentation.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.nocturne.store.core.util.CurrencyFormatter
import com.nocturne.store.domain.model.CartItem
import com.nocturne.store.presentation.components.EmptyState
import com.nocturne.store.presentation.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onBack: () -> Unit
) {
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val total      = cartItems.sumOf { it.lineTotal }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title  = { Text("Cart (${cartItems.sumOf { it.quantity }})", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                actions = {
                    if (cartItems.isNotEmpty()) {
                        TextButton(onClick = { cartViewModel.clearCart() }) {
                            Text("Clear all", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartBottomBar(total = total, onCheckout = { cartViewModel.clearCart() })
            }
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            EmptyState(
                message  = "Your cart is empty.\nStart shopping!",
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems, key = { it.productId }) { item ->
                    CartItemRow(
                        item      = item,
                        onRemove  = { cartViewModel.removeFromCart(item.productId) },
                        onInc     = { cartViewModel.increment(item.productId) },
                        onDec     = { cartViewModel.decrement(item.productId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onRemove: () -> Unit,
    onInc: () -> Unit,
    onDec: () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model              = item.thumbnail,
                contentDescription = item.title,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = item.title,
                    style      = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines   = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text  = CurrencyFormatter.format(item.price),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Quantity control
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick  = onDec,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Outlined.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                    }
                    Text(
                        text      = "${item.quantity}",
                        modifier  = Modifier.padding(horizontal = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    FilledTonalIconButton(
                        onClick  = onInc,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Text(
                    text       = CurrencyFormatter.format(item.lineTotal),
                    style      = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun CartBottomBar(total: Double, onCheckout: () -> Unit) {
    Surface(
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text  = "Total",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text       = CurrencyFormatter.format(total),
                    style      = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color      = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick  = onCheckout,
                modifier = Modifier.height(48.dp)
            ) {
                Text("Checkout", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
