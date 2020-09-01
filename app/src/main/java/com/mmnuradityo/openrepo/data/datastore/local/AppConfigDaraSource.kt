package com.mmnuradityo.openrepo.data.datastore.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class AppConfigDaraSource private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppConfigDaraSource", Context.MODE_PRIVATE)

    var isDarkMode: Boolean
        get() = sharedPreferences.getBoolean("isDarkMode", false)
        set(isDarkMode) {
            sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply()
        }

    companion object {
        @Volatile
        private var INSTANCE: AppConfigDaraSource? = null

        fun init(context: Context) =
            INSTANCE ?: synchronized(AppConfigDaraSource::class.java) {
                INSTANCE ?: AppConfigDaraSource(context)
                    .also { INSTANCE = it }
            }

        fun getInstance() = INSTANCE!!
    }

}