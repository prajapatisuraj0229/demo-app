package com.nocturne.store.core.ui

/**
 * Unified UI state wrapper for async operations backed by API/repositories.
 */
sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String, val isNetworkError: Boolean = false) : Resource<Nothing>
    data class Empty(val message: String) : Resource<Nothing>
}
