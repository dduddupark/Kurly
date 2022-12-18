package com.example.kurly.data.source

import com.example.kurly.data.*
import com.example.kurly.data.local.ProductDao
import com.example.kurly.data.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Kurly
 * Class: DefaultRepository
 * Created by bluepark on 2022/12/13.
 *
 * Description:
 */

class DefaultRepository constructor(
    private val productDao: ProductDao,
    private val remoteDataSource: RemoteDataSource,
) : Repository {

    val allProducts: Flow<List<Product>> = productDao.getAll()

    override suspend fun updateProductLike(product: Product) =
        productDao.update(product)

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

                                    CoroutineScope(Dispatchers.IO).launch {
                                        Timber.d("name= ${product.name}")
                                        product.id?.let { id ->
                                            if (productDao.getItemById(id) == null) {
                                                productDao.insert(product)
                                            } else {
                                                product.isLike =
                                                    productDao.getItemById(id)?.isLike ?: false
                                            }
                                        }
                                    }
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
