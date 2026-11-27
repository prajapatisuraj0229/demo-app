package com.nocturne.store.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nocturne.store.core.ui.Resource
import com.nocturne.store.presentation.components.*
import com.nocturne.store.presentation.viewmodel.CartViewModel
import com.nocturne.store.presentation.viewmodel.ProductViewModel
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val productsState   by viewModel.products.collectAsStateWithLifecycle()
    val categoriesState by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCat     by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val currentPage     by viewModel.currentPage.collectAsStateWithLifecycle()
    val cartItems       by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val cartCount        = cartItems.sumOf { it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "NOCTURNE",
                        fontWeight = FontWeight.Black,
                        color      = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search")
                    }
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) Badge { Text("$cartCount") }
                        }
                    ) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Category chips
            if (categoriesState is Resource.Success) {
                val cats = (categoriesState as Resource.Success).data
                CategoryChips(
                    categories = cats,
                    selected   = selectedCat,
                    onSelect   = { viewModel.selectCategory(it) },
                    modifier   = Modifier.padding(vertical = 8.dp)
                )
            }

            when (val state = productsState) {
                is Resource.Loading -> LoadingGrid(modifier = Modifier.weight(1f))

                is Resource.Error   -> ErrorState(
                    message  = state.message,
                    onRetry  = { viewModel.loadProducts() },
                    modifier = Modifier.weight(1f)
                )

                is Resource.Empty   -> EmptyState(
                    message  = state.message,
                    modifier = Modifier.weight(1f)
                )

                is Resource.Success -> {
                    val data       = state.data
                    val totalPages = ceil(data.total / 20.0).toInt()

                    LazyVerticalGrid(
                        columns             = GridCells.Fixed(2),
                        modifier            = Modifier.weight(1f),
                        contentPadding      = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(data.products) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onProductClick(product.id) }
                            )
                        }
                    }

                    PaginationBar(
                        currentPage = currentPage,
                        totalPages  = totalPages,
                        onPrev      = { viewModel.previousPage() },
                        onNext      = { viewModel.nextPage(data.total) }
                    )
                }
            }
        }
    }
}
