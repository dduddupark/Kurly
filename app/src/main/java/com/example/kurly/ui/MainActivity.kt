package com.example.kurly.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.example.kurly.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var pageNum = 1

    private lateinit var viewDataBinding: ActivityMainBinding

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
                pageNum++
                getData()
            }
        })

        observableData()

        getData()
    }

    private fun getData() {
        viewModel.getSections(pageNum)
    }

    private fun observableData() {
        viewModel.sectionInfo.observe(this) { sectionInfo ->
            sectionInfo.sectionList?.let {

                if (pageNum == 1) {
                    viewDataBinding.llContent.removeAllViews()
                }

                for (section in sectionInfo.sectionList) {
                    Timber.d("section=$section")
                    val productView = ProductView(this).apply {
                        this.setData(section)
                    }
                    viewDataBinding.llContent.addView(productView)
                    Timber.d("addView")
                }
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            viewDataBinding.isLoading = isLoading
        }
    }
}