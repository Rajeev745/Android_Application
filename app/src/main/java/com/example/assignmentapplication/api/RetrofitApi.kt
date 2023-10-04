package com.example.assignmentapplication.api

import com.example.assignmentapplication.models.UserData
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitApi {

    @GET("/posts/")
    fun getData(): Call<List<UserData>>
}