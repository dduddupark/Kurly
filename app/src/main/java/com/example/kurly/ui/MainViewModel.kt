package com.example.kurly.ui

import androidx.lifecycle.*
import com.example.kurly.data.Event
import com.example.kurly.data.Product
import com.example.kurly.data.SectionInfo
import com.example.kurly.data.source.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Kurly
 * Class: MainViewModel
 * Created by bluepark on 2022/12/12.
 *
 * Description:
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DefaultRepository
) : ViewModel() {

    val productLikeList = repository.allProducts.asLiveData()

    var itemClickPosition = 0

    private val _sectionList = MutableLiveData<Event<SectionInfo>>()
    val sectionList: LiveData<Event<SectionInfo>> = _sectionList

    private val _loading = MutableLiveData<Event<Boolean>>()
    val loading: LiveData<Event<Boolean>> = _loading

    fun updateProductLike(position: Int, product: Product) = CoroutineScope(Dispatchers.IO).launch {
        itemClickPosition = position
        product.isLike = !product.isLike
        repository.updateProductLike(product)
    }

    fun canNextPage(page: Int): Boolean {
        var canNext = false
        sectionList.value?.peekContent()?.page?.nextPage?.let {
            Timber.d("it = $it, page = $page")
            if (page <= it) {
                canNext = true
            }
        }
        return canNext
    }

    fun getSections(page: Int) {
        _loading.value = Event(true)

        viewModelScope.launch {
            repository.getSections(page).run {
                _loading.value = Event(false)
                this?.let {
                    _sectionList.value = Event(it)
                }
            }
        }
    }
}