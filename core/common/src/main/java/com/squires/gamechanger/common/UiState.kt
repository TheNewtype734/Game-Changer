package com.squires.gamechanger.common

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Error<T>(val message: String, val cachedData: T? = null) : UiState<T>
    data class Success<T>(val data: T) : UiState<T>
}
