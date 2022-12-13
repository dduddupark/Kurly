package com.example.kurly.data.source

import com.example.kurly.data.Result
import com.example.kurly.data.Section

interface Repository {

    suspend fun getSections(page: Int): Result<Section>

}