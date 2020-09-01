package com.mmnuradityo.openrepo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.*
import com.mmnuradityo.openrepo.data.datastore.local.AppConfigDaraSource
import com.mmnuradityo.openrepo.data.datastore.remote.RemoteDataSource
import com.mmnuradityo.openrepo.data.datastore.local.LoginDataSource

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class OpenRepoComponent(application: Application) : AppComponent {

    private val remoteDataSource = RemoteDataSource()
    private val loginDataSource = LoginDataSource.init(application)
    private val appConfigDaraSource = AppConfigDaraSource.init(application)

    override fun getRemoteDataSource(): RemoteDataSource = remoteDataSource

    override fun getLoginDataSource(): LoginDataSource = loginDataSource

    override fun getAppConfigDaraSource(): AppConfigDaraSource = appConfigDaraSource

    override fun setupDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) setDefaultNightMode(MODE_NIGHT_YES) else setDefaultNightMode(MODE_NIGHT_NO)
    }

}

interface AppComponent {

    fun getRemoteDataSource(): RemoteDataSource

    fun getLoginDataSource(): LoginDataSource

    fun getAppConfigDaraSource(): AppConfigDaraSource

    fun setupDarkMode(isDarkMode: Boolean)
}