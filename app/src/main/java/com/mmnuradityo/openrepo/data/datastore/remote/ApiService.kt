package com.mmnuradityo.openrepo.data.datastore.remote

import com.mmnuradityo.openrepo.data.model.GithubProfile
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.data.model.follower.Follower
import com.mmnuradityo.openrepo.data.model.search.Search
import com.mmnuradityo.openrepo.utils.Constant.BASE_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit.SECONDS

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
enum class ApiService {
    INSTANCE;

    companion object {
        @JvmStatic
        fun getInstance() =
            INSTANCE

        private var userName: String = ""
    }

    private var api: Api
    private var client: OkHttpClient

    init {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        client = clientRequest(logInterceptor)

        api = apiService()
    }

    private fun apiService() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

    private fun clientRequest(interceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, SECONDS)
        .readTimeout(60, SECONDS)
        .addInterceptor(interceptor)
        .build()

    fun setUser(userName: String) {
        Companion.userName = userName
    }

    fun login(userName: String, password: String): Observable<JSONObject> {
        setUser(userName)
        client = clientRequest(
            BasicAuthInterceptor(
                userName,
                password
            )
        )
        return apiService().login()
    }

    fun getProfile() =
        api.getProfile(userName)

    fun getRepositories(page: Int, perPage: Int) =
        api.getReposData(userName, page, perPage)

    fun getFollowers() = api.getFollowers(userName)

    fun getFollowing() = api.getFollowing(userName)

    fun getSearch(repoName: String) = api.getSearch("$repoName+user:$userName")

    interface Api {
        @GET(".")
        fun login(): Observable<JSONObject>

        @GET("/users/{username}")
        fun getProfile(
            @Path("username") username: String
        ): Observable<GithubProfile>

        @GET("/users/{username}/repos")
        fun getReposData(
            @Path("username") username: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
        ): Observable<List<GithubRepository>>

        @GET("/users/{username}/followers")
        fun getFollowers(
            @Path("username") username: String
        ): Observable<List<Follower>>

        @GET("/users/{username}/following")
        fun getFollowing(
            @Path("username") username: String
        ): Observable<List<Follower>>

        @GET("/search/repositories")
        fun getSearch(
            @Query("q") query: String
        ): Observable<Search>
    }

}