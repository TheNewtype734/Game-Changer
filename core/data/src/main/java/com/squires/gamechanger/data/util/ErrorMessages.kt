package com.squires.gamechanger.data.util

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(): String = when (this) {
    is UnknownHostException -> "No internet connection. Check your network and try again."
    is SocketTimeoutException -> "Connection timed out. Try again."
    is HttpException -> when (code()) {
        404 -> "Content not found."
        429 -> "Too many requests. Wait a moment and try again."
        in 500..599 -> "Server error. Try again later."
        else -> "Network error (${code()}). Try again."
    }
    else -> "Something went wrong. Try again."
}
