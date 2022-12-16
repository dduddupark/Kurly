package com.example.kurly.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kurly.data.Product
import com.example.kurly.data.SectionType
import com.example.kurly.databinding.ItemProductBigBinding
import com.example.kurly.databinding.ItemProductSmallBinding
import timber.log.Timber

/**
 * Kurly
 * Class: ProductAdapter
 * Created by bluepark on 2022/12/14.
 *
 * Description:
 */

class ProductAdapter(val type: String) :
    ListAdapter<Product, RecyclerView.ViewHolder>(ProductDiffUtil) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        Timber.d("item = $item")
        if (holder is SmallViewHolder) {
            holder.bind(item)
        } else if (holder is BigViewHolder) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (type) {
            SectionType.Grid.type,
            SectionType.Horizontal.type -> {
                return SmallViewHolder.from(parent)
            }
            SectionType.Vertical.type -> {
                return BigViewHolder.from(parent)
            }
        }

        return SmallViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class SmallViewHolder(private val binding: ItemProductSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
        }

        companion object {
            fun from(parent: ViewGroup): SmallViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductSmallBinding.inflate(layoutInflater, parent, false)
                binding.executePendingBindings()
                return SmallViewHolder(binding)
            }
        }
    }

    class BigViewHolder(private val binding: ItemProductBigBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
        }

        companion object {
            fun from(parent: ViewGroup): BigViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBigBinding.inflate(layoutInflater, parent, false)
                binding.executePendingBindings()
                return BigViewHolder(binding)
            }
        }
    }

    object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}