package com.umc.sweepic.util.network

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Fail(val statusCode: Int, val message: String) : NetworkResult<Nothing>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
}

inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        action(data)
    }
    return this
}

inline fun <T> NetworkResult<T>.onFail(resultCode: (Int) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Fail) {
        resultCode(this.statusCode)
    }
    return this
}

inline fun <T> NetworkResult<T>.onError(action: (Exception) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Fail) {
        action(IllegalArgumentException("code : ${this.statusCode}, message : ${this.message}"))
    } else if (this is NetworkResult.Error) {
        action(this.exception)
    }
    return this
}

inline fun <T> NetworkResult<T>.onException(action: (Exception) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) {
        action(this.exception)
    }
    return this
}