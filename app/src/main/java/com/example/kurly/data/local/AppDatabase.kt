package com.example.kurly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kurly.data.Product

/**
 * Kurly
 * Class: AppDatabase
 * Created by bluepark on 2022/12/17.
 *
 * Description:
 */
@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}