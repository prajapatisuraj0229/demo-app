package com.nocturne.store.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nocturne.store.presentation.screens.cart.CartScreen
import com.nocturne.store.presentation.screens.home.HomeScreen
import com.nocturne.store.presentation.screens.product.ProductDetailScreen
import com.nocturne.store.presentation.screens.search.SearchScreen
import com.nocturne.store.presentation.viewmodel.CartViewModel
import com.nocturne.store.presentation.viewmodel.ProductViewModel

@Composable
fun NocturneNavGraph() {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel: CartViewModel       = viewModel()

    NavHost(
        navController    = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel    = productViewModel,
                cartViewModel = cartViewModel,
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onCartClick    = { navController.navigate(Screen.Cart.route) },
                onSearchClick  = { navController.navigate(Screen.Search.route) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onProductClick = { id -> navController.navigate(Screen.ProductDetail.createRoute(id)) },
                onBack         = { navController.popBackStack() },
                cartViewModel  = cartViewModel
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                cartViewModel = cartViewModel,
                onBack        = { navController.popBackStack() }
            )
        }

        composable(
            route     = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStack ->
            val productId = backStack.arguments?.getInt("productId") ?: return@composable
            ProductDetailScreen(
                productId     = productId,
                cartViewModel = cartViewModel,
                onBack        = { navController.popBackStack() },
                onCartClick   = { navController.navigate(Screen.Cart.route) }
            )
        }
    }
}
