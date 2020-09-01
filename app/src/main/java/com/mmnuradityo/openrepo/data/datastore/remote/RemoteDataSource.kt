package com.mmnuradityo.openrepo.data.datastore.remote

import com.mmnuradityo.openrepo.data.model.GithubProfile
import com.mmnuradityo.openrepo.data.model.follower.Follower
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class RemoteDataSource :
    RemoteData {

    private val apiService = ApiService.getInstance()

    override fun setUser(userName: String) {
        apiService.setUser(userName)
    }

    override fun login(
        userName: String,
        password: String,
        onSuccess: Consumer<JSONObject>,
        onError: Consumer<Throwable>
    ): Disposable = apiService.login(userName, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)

    override fun getProfile(
        onSuccess: Consumer<GithubProfile>,
        onError: Consumer<Throwable>
    ): Disposable = apiService.getProfile()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)

    override fun getRepositories(
        page: Int,
        perPage: Int,
        onSuccess: Consumer<List<GithubRepository>>,
        onError: Consumer<Throwable>
    ): Disposable = apiService.getRepositories(page, perPage)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)

    override fun getFollowers(
        onSuccess: Consumer<List<Follower>>,
        onError: Consumer<Throwable>
    ): Disposable = apiService.getFollowers()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)

    override fun getFollowing(
        onSuccess: Consumer<List<Follower>>,
        onError: Consumer<Throwable>
    ): Disposable = apiService.getFollowing()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)

    override fun getSearch(
        repoName: String,
        onSuccess: Consumer<List<GithubRepository>>,
        onError: Consumer<Throwable>
    ): Disposable = apiService.getSearch(repoName)
        .map { it.items!! }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onSuccess, onError)

}

interface RemoteData {
    fun setUser(userName: String)

    fun login(
        userName: String,
        password: String,
        onSuccess: Consumer<JSONObject>,
        onError: Consumer<Throwable>
    ): Disposable

    fun getProfile(
        onSuccess: Consumer<GithubProfile>,
        onError: Consumer<Throwable>
    ): Disposable

    fun getRepositories(
        page: Int,
        perPage: Int,
        onSuccess: Consumer<List<GithubRepository>>,
        onError: Consumer<Throwable>
    ): Disposable

    fun getFollowers(
        onSuccess: Consumer<List<Follower>>,
        onError: Consumer<Throwable>
    ): Disposable

    fun getFollowing(
        onSuccess: Consumer<List<Follower>>,
        onError: Consumer<Throwable>
    ): Disposable

    fun getSearch(
        repoName: String,
        onSuccess: Consumer<List<GithubRepository>>,
        onError: Consumer<Throwable>
    ): Disposable
}