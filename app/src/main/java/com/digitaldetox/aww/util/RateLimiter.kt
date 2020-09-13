package com.digitaldetox.aww.util

import android.os.SystemClock
import android.util.Log
import androidx.collection.ArrayMap

import java.util.concurrent.TimeUnit

private const val TAG = "lgx_RateLimiter"


class RateLimiter<in KEY>(timeout: Int, timeUnit: TimeUnit) {
    private val timestamps = ArrayMap<KEY, Long>()
    private val timeout = timeUnit.toMillis(timeout.toLong())
    @Synchronized
    fun shouldFetch(key: KEY): Boolean {

        Log.d(TAG, "shouldFetch: 20: called. key --> ${key}")

        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null) {

            Log.d(TAG, "shouldFetch: 26: lastFetched ${lastFetched == null} ")

            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            Log.d(
                TAG,
                "shouldFetch: 32: now - lastFetched > timeout ${now - lastFetched > timeout} "
            )
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now(): Long {
        Log.d(TAG, "now: 40: ${SystemClock.uptimeMillis()}")
        return SystemClock.uptimeMillis()
    }

    @Synchronized
    fun reset(key: KEY) {
        Log.d(TAG, "reset: reset is called in RateLimiter class.  key --> ${key}")
        timestamps.remove(key)
    }
}