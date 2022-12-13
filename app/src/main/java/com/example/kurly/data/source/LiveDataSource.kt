package com.example.kurly.data.source

import androidx.lifecycle.LiveData
import com.example.kurly.data.Event
import com.example.kurly.data.Result


/**
 * SemoWork
 * Class: LiveDataSource
 * Created by YAP on 2020-05-27.
 * yap company Ltd
 * youngsoo.kim@yap.net
 * Description:
 */
interface LiveDataSource {
    fun getIntentLiveData(): LiveData<Event<Result<String>>>
    fun postIntentLiveData(data: Result<String>)
}