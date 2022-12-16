package com.example.kurly.data.remote

import com.example.kurly.data.ProductInfo
import com.example.kurly.data.Result
import com.example.kurly.data.SectionInfo

interface RemoteDataSource {

    suspend fun getSections(page: Int): Result<SectionInfo>

    suspend fun getSectionProducts(sectionId: Int): Result<ProductInfo>
}