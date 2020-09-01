package com.mmnuradityo.openrepo.data.repository

import com.mmnuradityo.openrepo.data.datastore.local.AppConfigDaraSource
import com.mmnuradityo.openrepo.data.datastore.local.LoginDataSource
import com.mmnuradityo.openrepo.data.datastore.remote.RemoteData
import com.mmnuradityo.openrepo.data.model.GithubProfile
import com.mmnuradityo.openrepo.data.model.follower.Follower
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import io.reactivex.rxjava3.functions.Consumer
import org.json.JSONObject

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class Repository private constructor(
    private val remoteData: RemoteData,
    private val loginData: LoginDataSource?,
    private val appConfig: AppConfigDaraSource?
) {

    companion object {
        private var INSTANCE: Repository? = null

        @JvmStatic
        fun init(
            remoteData: RemoteData,
            loginData: LoginDataSource?,
            appConfig: AppConfigDaraSource?
        ) =
            INSTANCE ?: synchronized(Repository::class.java) {
                INSTANCE ?: Repository(remoteData, loginData, appConfig).also { INSTANCE = it }
            }

        @JvmStatic
        fun detach() {
            INSTANCE = null
        }
    }

    init {
        loginData?.let {
            it.userName?.let { userName ->
                remoteData.setUser(userName)
            }
        }
    }

    var isLogin: Boolean?
        get() = loginData?.isLogin
        set(isLogin) {
            loginData?.isLogin = isLogin!!
        }

    var userName: String?
        get() = loginData?.userName
        set(userName) {
            loginData?.userName = userName!!
        }

    var isDarkMode: Boolean?
        get() = appConfig?.isDarkMode
        set(isDarkMode) {
            appConfig?.isDarkMode = isDarkMode!!
        }

    fun login(
        userName: String,
        password: String,
        onSuccess: Consumer<JSONObject>,
        onError: Consumer<Throwable>
    ) =
        remoteData.login(userName, password, onSuccess, onError)

    fun getProfile(onSuccess: Consumer<GithubProfile>, onError: Consumer<Throwable>) =
        remoteData.getProfile(onSuccess, onError)

    fun getRepositories(
        page: Int,
        perPage: Int,
        onSuccess: Consumer<List<GithubRepository>>,
        onError: Consumer<Throwable>
    ) =
        remoteData.getRepositories(page, perPage, onSuccess, onError)

    fun getFollowers(
        onSuccess: Consumer<List<Follower>>,
        onError: Consumer<Throwable>
    ) =
        remoteData.getFollowers(onSuccess, onError)


    fun getFollowing(
        onSuccess: Consumer<List<Follower>>,
        onError: Consumer<Throwable>
    ) =
        remoteData.getFollowing(onSuccess, onError)

    fun getSearch(
        repoName: String,
        onSuccess: Consumer<List<GithubRepository>>,
        onError: Consumer<Throwable>
    ) =
        remoteData.getSearch(repoName, onSuccess, onError)


}