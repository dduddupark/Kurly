package com.example.kurly.data.local

import androidx.room.*
import com.example.kurly.data.Product
import kotlinx.coroutines.flow.Flow


/**
 * Kurly
 * Class: ProductDao
 * Created by bluepark on 2022/12/17.
 *
 * Description:
 */
@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * from product WHERE id= :id")
    fun getItemById(id: Int): Product?

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)
}