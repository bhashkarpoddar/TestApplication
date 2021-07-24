package com.example.testappliaction.data.network.responses

sealed class ResponseResult<out T> {
    object Loading: ResponseResult<Nothing>()
    object Empty: ResponseResult<Nothing>()
    data class Success<T>(val result:T): ResponseResult<T>()
    data class Error<T>(val msg:T): ResponseResult<T>()
    object NoInternet: ResponseResult<Nothing>()
}
data class ResponseWrapper<out T>(val data: T?, val errorMsg: String?)