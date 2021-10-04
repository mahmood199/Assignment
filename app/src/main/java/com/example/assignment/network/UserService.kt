package com.example.assignment.network

import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET("/api/v0/users")
    suspend fun getUserDetails(): Response<String>

}
