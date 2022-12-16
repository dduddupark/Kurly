package com.example.kurly.ui

import androidx.lifecycle.*
import com.example.kurly.data.Event
import com.example.kurly.data.SectionInfo
import com.example.kurly.data.source.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val retrofit: DefaultRepository
) : ViewModel() {

    private val _sectionInfo = MutableLiveData<SectionInfo>()
    val sectionInfo: LiveData<SectionInfo> = _sectionInfo

    private val _loading = MutableLiveData<Event<Boolean>>()
    val loading: LiveData<Boolean?> = _loading.map { event: Event<Boolean>? ->
        event?.getContentIfNotHandled()
    }

    fun canNextPage(page: Int): Boolean {
        var canNext = false
        Timber.d("page = ${sectionInfo.value?.page?.nextPage}")
        sectionInfo.value?.page?.nextPage?.let {
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
            retrofit.getSections(page).run {
                _loading.value = Event(false)
                this?.let {
                    _sectionInfo.value = it
                }
            }
        }
    }
}