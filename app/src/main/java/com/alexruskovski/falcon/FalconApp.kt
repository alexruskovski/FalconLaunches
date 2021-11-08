package com.alexruskovski.falcon

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by Alexander Ruskovski on 15/08/2021
 */

@HiltAndroidApp
class FalconApp: Application(){

    override fun onCreate() {
        super.onCreate()
    }

}