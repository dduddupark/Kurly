package com.example.kurly.data.remote

import com.example.kurly.data.Section
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    //섹션 목록 불러오기
    @GET("sections")
    suspend fun getEvaluationYN(@Query("page") page: Int): Section

}