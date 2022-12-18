package com.example.kurly.ui

import androidx.recyclerview.widget.RecyclerView

/**
 * Kurly
 * Class: BaseAdapter
 * Created by bluepark on 2022/12/18.
 *
 * Description:
 */
abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract fun setData(list: List<Any>?)
}