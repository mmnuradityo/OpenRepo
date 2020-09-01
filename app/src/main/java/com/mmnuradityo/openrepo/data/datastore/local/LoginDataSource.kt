package com.mmnuradityo.openrepo.data.datastore.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class LoginDataSource private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LoginDataSource", Context.MODE_PRIVATE)

    var isLogin: Boolean
        get() = sharedPreferences.getBoolean("isLogin", false)
        set(isLogin) {
            sharedPreferences.edit().putBoolean("isLogin", isLogin).apply()
        }

    var userName: String?
        get() = sharedPreferences.getString("userName", "")
        set(userName) {
            sharedPreferences.edit().putString("userName", userName).apply()
        }

    fun logout() {
        isLogin = false
        userName = ""
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginDataSource? = null

        fun init(context: Context) =
            INSTANCE ?: synchronized(LoginDataSource::class.java) {
                INSTANCE ?: LoginDataSource(context)
                    .also { INSTANCE = it }
            }

        fun getInstance() = INSTANCE!!
    }

}
