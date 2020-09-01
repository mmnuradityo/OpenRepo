package com.mmnuradityo.openrepo

import android.app.Application
import com.mmnuradityo.openrepo.data.repository.Repository

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class OpenRepoApp : Application() {

    companion object {
        private var INSTANCE: OpenRepoApp? = null

        fun getInstance() = INSTANCE!!
    }

    private lateinit var appComponent: AppComponent
    private lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        appComponent = OpenRepoComponent(this)
        repository = Repository.attach(
            appComponent.getRemoteDataSource(),
            appComponent.getLoginDataSource(),
            appComponent.getAppConfigDaraSource()
        )

        setDarkMode(appComponent.getAppConfigDaraSource().isDarkMode)

        INSTANCE = this
    }

    fun getRepository() = repository

    fun setDarkMode(isDarkMode: Boolean) {
        appComponent.setupDarkMode(isDarkMode)
    }
}