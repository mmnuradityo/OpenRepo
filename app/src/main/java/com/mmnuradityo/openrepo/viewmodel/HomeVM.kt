package com.mmnuradityo.openrepo.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.mmnuradityo.openrepo.base.BaseVM
import com.mmnuradityo.openrepo.data.model.GithubProfile
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.data.repository.Repository
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.utils.search.SearchKeyboardUtils
import com.mmnuradityo.openrepo.utils.SingleLiveEvent
import io.reactivex.rxjava3.functions.Consumer

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class HomeVM(application: Application, private val repository: Repository) :
    BaseVM(application, ViewState()) {

    private var perPage: Int = 15
    val profile: ObservableField<GithubProfile> = ObservableField()
    val repoList: ObservableList<GithubRepository> = ObservableArrayList()
    val repoLoading: ObservableField<Boolean> = ObservableField()
    val openRepo = SingleLiveEvent<GithubRepository>()

    fun setupLoadDataSize(perPage: Int) {
        this.perPage = perPage
    }

    override fun start() {
        super.start()
        getProfile()
    }

    private fun getProfile() {
        repository.getProfile(
            Consumer {
                profile.set(it)

                Handler(Looper.getMainLooper()).postDelayed({
                    setViewState(isSuccess = true)
                }, 1000)

            }, Consumer { setViewState(isError = it) })
    }

    fun getRepoData(page: Int) {
        repository.getRepositories(page, perPage,
            Consumer {
                repoLoading.set(it.isNotEmpty())
                compare(repoList, it)
            },
            Consumer {
                repoLoading.set(false)
                setViewState(isError = it)
            })
    }

    fun hideKeyboard() {
        SearchKeyboardUtils.getInstance().hide()
    }
}