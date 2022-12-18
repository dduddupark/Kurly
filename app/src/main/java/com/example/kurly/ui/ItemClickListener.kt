package com.example.kurly.ui

import com.example.kurly.data.Product

/**
 * Kurly
 * Class: ItemClickListener
 * Created by bluepark on 2022/12/18.
 *
 * Description:
 */
interface ItemClickListener {
    fun itemClick(position: Int, product: Product)
}