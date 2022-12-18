package com.example.kurly.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Kurly
 * Class: Product
 * Created by bluepark on 2022/12/13.
 *
 * Description:
 */
data class ProductInfo(
    @SerializedName("data")
    val productList: List<Product>? = null
)

@Entity
data class Product(
    @PrimaryKey val id: Int? = 0,
    val name: String? = "",
    val image: String? = "",
    val originalPrice: Int? = 0,
    val discountedPrice: Int? = 0,
    val isSoldOut: Boolean? = false,
    var discountPercent: String? = "",
    @ColumnInfo var isLike: Boolean = false
)

fun getDiscountPercent(originalPrice: Int, discountedPrice: Int): String {
    return if (originalPrice > 0 && discountedPrice > 0) {
        (100 - ((discountedPrice.toDouble() / originalPrice.toDouble()) * 100).toInt()).toString()
            .plus("%")
    } else {
        ""
    }
}


