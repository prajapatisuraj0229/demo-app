package com.nocturne.store.presentation.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nocturne.store.core.ui.Resource
import com.nocturne.store.core.util.CurrencyFormatter
import com.nocturne.store.data.remote.model.Product
import com.nocturne.store.domain.model.CartItem
import com.nocturne.store.presentation.components.ErrorState
import com.nocturne.store.presentation.components.LoadingGrid
import com.nocturne.store.presentation.viewmodel.CartViewModel
import com.nocturne.store.presentation.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    productViewModel: ProductViewModel = viewModel()
) {
    val state     by productViewModel.productDetail.collectAsStateWithLifecycle()
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val cartCount  = cartItems.sumOf { it.quantity }

    LaunchedEffect(productId) { productViewModel.loadProductDetail(productId) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title  = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                actions = {
                    BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is Resource.Loading -> LoadingGrid()
                is Resource.Error   -> ErrorState(message = s.message, onRetry = { productViewModel.loadProductDetail(productId) })
                is Resource.Empty   -> ErrorState(message = s.message)
                is Resource.Success -> ProductDetailContent(
                    product       = s.data,
                    isInCart      = cartViewModel.isInCart(s.data.id),
                    onAddToCart   = {
                        cartViewModel.addToCart(
                            CartItem(
                                productId = s.data.id,
                                title     = s.data.title,
                                price     = s.data.discountedPrice,
                                thumbnail = s.data.thumbnail
                            )
                        )
                    },
                    onGoToCart    = onCartClick
                )
            }
        }
    }
}

@Composable
private fun ProductDetailContent(
    product: Product,
    isInCart: Boolean,
    onAddToCart: () -> Unit,
    onGoToCart: () -> Unit
) {
    val scrollState = rememberScrollState()
    val images      = product.images.ifEmpty { listOf(product.thumbnail) }
    val pagerState  = rememberPagerState(pageCount = { images.size })

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {

        // Image pager
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                AsyncImage(
                    model              = images[page],
                    contentDescription = product.title,
                    contentScale       = ContentScale.Fit,
                    modifier           = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
            // Dots indicator
            if (images.size > 1) {
                Row(
                    modifier              = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    repeat(images.size) { i ->
                        Box(
                            modifier = Modifier
                                .size(if (i == pagerState.currentPage) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i == pagerState.currentPage) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outlineVariant
                                )
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {

            // Category chip
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text     = product.category.replaceFirstChar { it.uppercase() },
                    style    = MaterialTheme.typography.labelSmall,
                    color    = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text       = product.title,
                style      = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rating + stock row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Filled.Star,
                    contentDescription = null,
                    tint               = Color(0xFFF59E0B),
                    modifier           = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text  = "%.1f".format(product.rating),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text  = "${product.stock} in stock",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (product.stock > 10) Color(0xFF16A34A) else Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Price
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text       = CurrencyFormatter.format(product.discountedPrice),
                    style      = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color      = MaterialTheme.colorScheme.primary
                )
                if (product.discountPercentage >= 1.0) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text           = CurrencyFormatter.format(product.price),
                        style          = MaterialTheme.typography.bodyLarge,
                        color          = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text     = "-${product.discountPercentage.toInt()}%",
                            style    = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color    = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text  = "About this product",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text  = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Brand
            if (!product.brand.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text  = "Brand: ${product.brand}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Add to Cart / Go to Cart button
            Button(
                onClick  = if (isInCart) onGoToCart else onAddToCart,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text       = if (isInCart) "Go to Cart" else "Add to Cart",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
