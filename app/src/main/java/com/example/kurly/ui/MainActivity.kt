package com.example.kurly.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.example.kurly.data.EventObserver
import com.example.kurly.data.Product
import com.example.kurly.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var pageNum = 1

    private lateinit var viewDataBinding: ActivityMainBinding

    private var viewList = mutableListOf<ProductView>()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        viewDataBinding.swipeLayout.setOnRefreshListener {
            pageNum = 1
            getData()
            viewDataBinding.swipeLayout.isRefreshing = false
        }

        viewDataBinding.nsView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val lastChildView = v.getChildAt(v.childCount - 1) as View
            val diff: Int = lastChildView.bottom - (v.height + scrollY)

            if (diff == 0 && viewModel.canNextPage(pageNum)) {
                if (viewDataBinding.isLoading == false) {
                    pageNum++
                    getData()
                }
            }
        })

        observableData()

        getData()
    }

    private fun getData() {
        viewModel.getSections(pageNum)
    }

    private fun observableData() {

        viewModel.productLikeList.observe(this) {
            if (viewDataBinding.isLoading == false) {
                for (view in viewList) {
                    view.updateView(viewModel.itemClickPosition)
                }
            }
        }

        viewModel.sectionList.observe(this, EventObserver {
            it.sectionList?.let { list ->
                if (pageNum == 1) {
                    viewDataBinding.llContent.removeAllViews()
                }

                for (section in list) {
                    val productView = ProductView(this).apply {
                        this.setData(section, object : ItemClickListener {
                            override fun itemClick(position: Int, product: Product) {
                                viewModel.updateProductLike(position, product)
                            }
                        })
                    }

                    viewList.add(productView)
                    viewDataBinding.llContent.addView(productView)
                }
            }
        })

        viewModel.loading.observe(this, EventObserver {
            viewDataBinding.isLoading = it
        })
    }
}