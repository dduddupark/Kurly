package com.example.kurly

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurly.data.Result
import com.example.kurly.data.source.DefaultRepository
import com.example.kurly.data.toError
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
    private val retrofit: DefaultRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getSections(page: Int) {
        viewModelScope.launch {
            retrofit.getSections(page).run {
                when (this) {
                    is Result.Success -> {
                        Timber.d("data = ${this.data}")
                    }
                    is Result.Error -> {
                        Timber.d("exception = ${exception.toError()}")
                    }
                }
            }
        }
    }
}