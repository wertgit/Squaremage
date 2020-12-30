package com.bakkac.squaremage

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}