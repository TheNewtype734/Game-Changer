package com.squires.gamechanger.common

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val message: String, val cause: Throwable? = null, val cachedData: T? = null) : Result<T>()
    data object Loading : Result<Nothing>()
}
