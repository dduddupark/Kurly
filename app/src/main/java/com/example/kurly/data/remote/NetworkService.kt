package com.example.kurly.data.remote

import com.example.kurly.data.ProductInfo
import com.example.kurly.data.SectionInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    //섹션 목록 불러오기
    @GET("sections")
    suspend fun getSections(@Query("page") page: Int): SectionInfo

    @GET("section/products")
    suspend fun getSectionProducts(@Query("sectionId") sectionId: Int): ProductInfo
}