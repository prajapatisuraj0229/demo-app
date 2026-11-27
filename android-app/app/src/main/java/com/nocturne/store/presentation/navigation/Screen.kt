package com.nocturne.store.presentation.navigation

/** All routes in the app. */
sealed class Screen(val route: String) {
    object Home       : Screen("home")
    object Search     : Screen("search")
    object Cart       : Screen("cart")
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(id: Int) = "product/$id"
    }
}
