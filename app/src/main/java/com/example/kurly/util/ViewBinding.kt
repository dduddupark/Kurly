package com.example.kurly.util

import android.graphics.Paint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.kurly.R
import java.text.DecimalFormat

/**
 * Kurly
 * Class: Viewbinding
 * Created by bluepark on 2022/12/14.
 *
 * Description:
 */
@BindingAdapter("setUri")
fun setImageWithUri(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(it)
    }
}

@BindingAdapter("formatMoney")
fun getDecimalFormat(textView: TextView, number: Int) {
    val decimalFormat = DecimalFormat("#,###")
    textView.text = textView.context.getString(R.string.str_price, decimalFormat.format(number))
}

@BindingAdapter("textStrike")
fun setTextStrike(textView: TextView, isStrike: Boolean) {
    if (isStrike) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

object RecyclerBinding {

    /**
     * 아이템 설정
     *
     * @param recyclerView RecyclerView
     * @param adapter      RecyclerView.Adapter
     * @param dataList     설정할 아이템
     */
    @JvmStatic
    @BindingAdapter("android:recyclerViewAdapter", "android:items")
    fun bindItem(
        recyclerView: RecyclerView,
        adapter: ListAdapter<Any, Nothing>?,
        dataList: List<Any>?
    ) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }
        Log.d("bindItem", "adapter = $adapter")
        Log.d("bindItem", "dataList = $dataList")
        dataList?.let {
            adapter?.submitList(dataList as List<Any>?)
        }
    }
}