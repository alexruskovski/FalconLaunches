package com.alexruskovski.falcon.util

//
//import androidx.annotation.VisibleForTesting
//import androidx.test.espresso.idling.CountingIdlingResource
//
//
///**
// * Created by Alexander Ruskovski
// * Helper class to help us keep up the idle resource on CharacterFragment from the test
// */
//
//object TrackingIdleResource{
//
//    @VisibleForTesting
//    @JvmField val countingIdlingResource = CountingIdlingResource("GLOBAL-SCOPE")
//
//    fun increment(){
//        countingIdlingResource.increment()
//    }
//
//    fun decrement(){
//        if(!countingIdlingResource.isIdleNow)
//            countingIdlingResource.decrement()
//    }
//
//}