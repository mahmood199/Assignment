package com.example.assignment.repository

import android.content.Context
import com.examlounge.examloungeapp.network.ApiClient
import com.examlounge.examloungeapp.network.ApiUtils.getResponse
import com.example.assignment.network.ApiResult

class Repository {
    suspend fun getUserProfile(context: Context): ApiResult<String> {
        return getResponse(
            request = { ApiClient.getUserService(context).getUserDetails() },
            "Couldn't Fetch User Profile", 1
        )
    }

}