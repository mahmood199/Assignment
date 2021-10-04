package com.examlounge.examloungeapp.network

import android.util.Log
import com.example.assignment.network.ApiResult
import com.example.assignment.network.ErrorRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


object ApiUtils {
    @JvmStatic
    fun <T> parseError(response: Response<T>): ErrorRes.ApiError? {
        val converter: Converter<ResponseBody, ErrorRes.ApiError> = ApiClient.retrofit
            .responseBodyConverter(ErrorRes.ApiError::class.java, arrayOfNulls<Annotation>(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (ex: Exception) {
            null
        }
    }

    @JvmStatic
    fun <T> parseError1(response: Response<T>): ErrorRes.ApiError2? {
        val converter: Converter<ResponseBody, ErrorRes.ApiError2> = ApiClient.retrofit
            .responseBodyConverter(ErrorRes.ApiError2::class.java, arrayOfNulls<Annotation>(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun <T> getFlow(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String, errorType: Int
    ): Flow<ApiResult<T>> {
        return flow {
            emit(ApiResult.loading())
            val result = getResponse(request = request, defaultErrorMessage, errorType)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String, errorType: Int
    ): ApiResult<T> {
        try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return ApiResult.success(result.body())
            } else {
                if (errorType == 0) {
                    val errorResponse = parseError(result)
                    if (errorResponse is ErrorRes.ApiError)
                        return ApiResult.error(
                            errorResponse.desc ?: "Something went wrong",
                            errorResponse
                        )
                } else {
                    val errorResponse = parseError1(result)
                    if (errorResponse != null && errorResponse is ErrorRes.ApiError2)
                        return ApiResult.error(
                            errorResponse.errorMessage ?: "Something went wrong",
                            errorResponse
                        )
                    else
                        return ApiResult.error("Something went wrong", null)
                }
            }

        } catch (e: Throwable) {
            if (e is IOException)
                return ApiResult.error("Please Check Your Network Connection", null)
            Log.e("Api error", e.stackTraceToString())
        }
        return ApiResult.error(defaultErrorMessage, null)
    }
}