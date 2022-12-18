package com.example.kurly.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kurly.data.Product
import com.example.kurly.data.SectionType
import com.example.kurly.databinding.ItemProductBigBinding
import com.example.kurly.databinding.ItemProductSmallBinding
import timber.log.Timber

/**
 * Kurly
 * Class: ProductRecyclerAdapter
 * Created by bluepark on 2022/12/18.
 *
 * Description:
 */
class ProductRecyclerAdapter(val type: String) : BaseAdapter() {

    private var productList = arrayListOf<Product>()
    private var clickPosition = 0
    private var itemClickListener: ItemClickListener? = null

    override fun setData(list: List<Any>?) {
        list?.let {
            productList = it as ArrayList<Product>
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun notifyItem(position: Int) {
        clickPosition = position
        notifyItemChanged(clickPosition)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = productList[position]
        if (holder is SmallViewHolder) {
            holder.bind(position, item, itemClickListener)
        } else if (holder is BigViewHolder) {
            holder.bind(position, item, itemClickListener)
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
        return productList.size
    }

    class SmallViewHolder(private val binding: ItemProductSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, product: Product, listener: ItemClickListener?) {
            binding.product = product
            binding.ivHeart.setOnClickListener {
                Timber.d("position = $position, product = $product")
                listener?.itemClick(position, product)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SmallViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductSmallBinding.inflate(layoutInflater, parent, false)

                return SmallViewHolder(binding)
            }
        }
    }

    class BigViewHolder(private val binding: ItemProductBigBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, product: Product, listener: ItemClickListener?) {
            binding.product = product
            binding.ivHeart.setOnClickListener {
                Timber.d("position = $position, product = $product")
                listener?.itemClick(position, product)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): BigViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBigBinding.inflate(layoutInflater, parent, false)
                return BigViewHolder(binding)
            }
        }
    }
}