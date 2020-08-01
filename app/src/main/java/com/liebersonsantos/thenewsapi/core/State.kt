package com.liebersonsantos.thenewsapi.core

data class State<out T>(
    val status: Status,
    val loading:Boolean?,
    val data: T?,
    val error: Throwable?,
    val errorMessage: String?,
    val statusCode: Int?
) {
    companion object{
        fun <T> success(data: T?): State<T>{
            return State(Status.SUCCESS, loading = false, data = data, error = null, errorMessage = null, statusCode = null)
        }

        fun <T> error(error: Throwable): State<T>{
            return State(Status.ERROR, loading = false, data = null, error = error, errorMessage = null, statusCode = null)
        }

        fun <T> loading(loading: Boolean): State<T>{
            return State(Status.LOADING, loading = loading, data = null, error = null, errorMessage = null, statusCode = null)
        }

        fun <T> errorMessage(errorMessage: String, statusCode: Int): State<T>{
            return State(Status.ERROR, loading = false, data = null, error = null, errorMessage = errorMessage, statusCode = statusCode)
        }
    }
}