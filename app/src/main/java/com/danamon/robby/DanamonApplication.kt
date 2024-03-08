package com.danamon.robby

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DanamonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}