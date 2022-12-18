package com.example.kurly.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurly.R
import com.example.kurly.data.Product
import com.example.kurly.data.Section
import com.example.kurly.data.SectionType
import com.example.kurly.databinding.ViewProductBinding

/**
 * Kurly
 * Class: ProductView
 * Created by bluepark on 2022/12/15.
 *
 * Description:
 */
class ProductView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var viewDataBinding: ViewProductBinding

    init {
        val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = mInflater.inflate(R.layout.view_product, this, false)
        viewDataBinding = ViewProductBinding.bind(view).apply {
            this.rvList.apply {
                itemAnimator = null
            }
        }
        addView(viewDataBinding.root)
    }

    fun updateView(itemClickPosition: Int) {
        (viewDataBinding.productAdapter as ProductRecyclerAdapter).run {
            this.notifyItem(itemClickPosition)
        }
    }

    fun setData(section: Section, listener: ItemClickListener?) {

        viewDataBinding.section = section

        when (section.type) {
            SectionType.Grid.type -> {
                viewDataBinding.typeLayoutManager = GridLayoutManager(context, 2)
            }
            SectionType.Vertical.type -> {
                viewDataBinding.typeLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            SectionType.Horizontal.type -> {
                viewDataBinding.typeLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        val adapter = ProductRecyclerAdapter(section.type ?: "").apply {
            this.setItemClickListener(object : ItemClickListener {
                override fun itemClick(position: Int, product: Product) {
                    listener?.itemClick(position, product)
                }
            })
        }
        viewDataBinding.productAdapter = adapter

    }
}