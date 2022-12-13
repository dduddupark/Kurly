package com.example.kurly

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Kurly
 * Class: MyApplication
 * Created by bluepark on 2022/12/13.
 *
 * Description:
 */
@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}