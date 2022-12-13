package com.example.kurly.data.source

import android.content.Context
import com.example.kurly.data.Result
import com.example.kurly.data.Section


/**
 * BCMProject
 * Class: DefaultRepository
 * Created by 박수연 on 2021-04-16.
 *
 * Description: 총 데이터 관리 Class(ViewModel 쪽에서 호출하면 접근 하게되는 Class)
 */

class DefaultRepository constructor(
    private val applicationContext: Context,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getSections(page: Int): Result<Section> =
        remoteDataSource.getSections(page)

}
