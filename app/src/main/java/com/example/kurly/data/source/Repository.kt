package com.example.kurly.data.source

import com.example.kurly.data.ProductInfo
import com.example.kurly.data.Result
import com.example.kurly.data.SectionInfo

interface Repository {

    suspend fun getSections(page: Int): SectionInfo?

    suspend fun getSectionProducts(sectionId: Int): Result<ProductInfo>

}