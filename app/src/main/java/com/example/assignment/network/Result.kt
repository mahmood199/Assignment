package com.example.assignment.network

import com.google.gson.annotations.SerializedName

data class ApiResult<out T>(val status: Status, val data: T?, val error: ErrorRes?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ApiResult<T> {
            return ApiResult(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: ErrorRes?): ApiResult<T> {
            return ApiResult(Status.ERROR, null, error, message)
        }

        fun <T> loading(): ApiResult<T> {
            return ApiResult(Status.LOADING, null, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}
sealed class ErrorRes {

    data class ApiError(
        @SerializedName("error")
        val error: String?,
        @SerializedName("error_description")
        val desc: String?
    ):ErrorRes()

    data class ApiError2(
        @SerializedName("errorMessage")
        val errorMessage: String?,
        @SerializedName("errorType")
        val errorType: String?,
        @SerializedName("requestId")
        val requestId: String?,
        @SerializedName("timestamp")
        val timestamp: String?
    ):ErrorRes()
}