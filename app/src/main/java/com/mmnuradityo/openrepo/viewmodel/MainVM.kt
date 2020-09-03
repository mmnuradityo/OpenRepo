package com.mmnuradityo.openrepo.viewmodel

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Spanned
import android.view.animation.Animation
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.mmnuradityo.openrepo.base.BaseVM
import com.mmnuradityo.openrepo.data.model.GithubProfile
import com.mmnuradityo.openrepo.data.repository.Repository
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.utils.AppDetail
import com.mmnuradityo.openrepo.utils.SingleLiveEvent
import io.reactivex.rxjava3.functions.Consumer

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class MainVM(application: Application, private val repository: Repository) :
    BaseVM(application, ViewState()) {

    companion object {
        const val MENU_HOME = 1
        const val MENU_SEARCH = 2
        const val MENU_SETTINGS = 3
        const val MENU_LOGOUT = 4
    }

    private val profileSet = SingleLiveEvent<GithubProfile>()
    val profile: LiveData<GithubProfile> get() = profileSet
    val startAnim: ObservableField<Animation> = ObservableField()
    val appDetails: ObservableField<Spanned> = ObservableField()

    var isLogin: Boolean
        get() = repository.isLogin!!
        set(isLogin) {
            repository.isLogin = isLogin
        }

    var isDarkMode: Boolean
        get() = repository.isDarkMode!!
        set(isDarkMode) {
            repository.isDarkMode = isDarkMode
        }

    override fun start() {
        super.start()
        getProfile()
    }

    fun getAppDetail(context: Context) {
        appDetails.set(AppDetail.getAppDetail(context))
    }

    private fun getProfile() {
        addDisposable(
            repository.getProfile(
                Consumer {
                    profileSet.value = it

                    Handler(Looper.getMainLooper()).postDelayed({
                        setViewState(isSuccess = true)
                    }, 1500)

                }, Consumer { setViewState(isError = it) })
        )
    }

    fun logout() {
        repository.isLogin = false
        repository.userName = ""
    }

}