package com.example.kurly.data.source

import com.example.kurly.data.Result
import com.example.kurly.data.Section

/**
 * BCMProject
 * Class: RemoteDatasource
 * Created by 박수연 on 2021-04-16.
 *
 * Description: API 호출 Interface
 */
interface RemoteDataSource {
    suspend fun getSections(page: Int): Result<Section>
}