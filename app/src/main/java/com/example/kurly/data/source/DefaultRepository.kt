package com.example.kurly.data.source

import com.example.kurly.data.ProductInfo
import com.example.kurly.data.Result
import com.example.kurly.data.SectionInfo
import com.example.kurly.data.getDiscountPercent
import com.example.kurly.data.remote.RemoteDataSource


/**
 * Kurly
 * Class: DefaultRepository
 * Created by bluepark on 2022/12/13.
 *
 * Description:
 */

class DefaultRepository constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getSections(page: Int): SectionInfo? {

        remoteDataSource.getSections(page).run {
            if (this is Result.Success) {
                this.data.sectionList?.map {
                    it.id?.let { id ->
                        remoteDataSource.getSectionProducts(id).run {
                            if (this is Result.Success) {
                                this.data.productList?.map { product ->
                                    product.discountPercent = getDiscountPercent(
                                        product.originalPrice ?: 0,
                                        product.discountedPrice ?: 0
                                    )
                                }

                                it.products = this.data.productList
                            }
                        }
                    }
                }

                return this.data
            }
        }

        return null
    }

    override suspend fun getSectionProducts(sectionId: Int): Result<ProductInfo> =
        remoteDataSource.getSectionProducts(sectionId)
}
