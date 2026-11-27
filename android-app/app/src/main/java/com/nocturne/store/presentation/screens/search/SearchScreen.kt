package com.nocturne.store.presentation.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nocturne.store.core.ui.Resource
import com.nocturne.store.presentation.components.*
import com.nocturne.store.presentation.viewmodel.CartViewModel
import com.nocturne.store.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onProductClick: (Int) -> Unit,
    onBack: () -> Unit,
    cartViewModel: CartViewModel,
    searchViewModel: SearchViewModel = viewModel()
) {
    val query   by searchViewModel.query.collectAsStateWithLifecycle()
    val results by searchViewModel.results.collectAsStateWithLifecycle()
    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    OutlinedTextField(
                        value         = query,
                        onValueChange = { searchViewModel.onQueryChange(it) },
                        placeholder   = { Text("Search products…") },
                        singleLine    = true,
                        leadingIcon   = { Icon(Icons.Outlined.Search, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = MaterialTheme.shapes.medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                query.length < 2 -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Type to search products",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                results == null || results is Resource.Loading ->
                    LoadingGrid()

                results is Resource.Error ->
                    ErrorState(message = (results as Resource.Error).message)

                results is Resource.Empty ->
                    EmptyState(message = (results as Resource.Empty).message)

                results is Resource.Success -> {
                    val products = (results as Resource.Success).data.products
                    LazyVerticalGrid(
                        columns              = GridCells.Fixed(2),
                        contentPadding       = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement  = Arrangement.spacedBy(10.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onProductClick(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
