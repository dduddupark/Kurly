package com.example.kurly.data

import com.google.gson.annotations.SerializedName
import timber.log.Timber

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

data class Product(
    val id: Int? = 0,
    val name: String? = "",
    val image: String? = "",
    val originalPrice: Int? = 0,
    val discountedPrice: Int? = 0,
    val isSoldOut: Boolean? = false,
    var discountPercent: String? = ""
)

fun getDiscountPercent(originalPrice: Int, discountedPrice: Int): String {
    return if (originalPrice > 0 && discountedPrice > 0) {
        val p =
            (100 - ((discountedPrice.toDouble() / originalPrice.toDouble()) * 100).toInt()).toString()
                .plus("%")
        Timber.d(p)
        p
    } else {
        Timber.d("else")
        ""
    }
}


