package com.mmnuradityo.openrepo.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.mmnuradityo.openrepo.base.BaseVM
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.data.repository.Repository
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.utils.search.SearchKeyboardUtils
import com.mmnuradityo.openrepo.utils.SingleLiveEvent
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class SearchVM(application: Application, private val repository: Repository) :
    BaseVM(application, ViewState()) {

    val repoList: ObservableList<GithubRepository> = ObservableArrayList()
    val openRepo = SingleLiveEvent<GithubRepository>()
    val textRepo : ObservableField<String> = ObservableField("Search repository")
    private var disposable: Disposable? = null

    fun getRepoData(repoName: String) {
        disposable?.dispose()
        setViewState(isLoad = true)
        repoList.clear()

        disposable = repository.getSearch(repoName,
            Consumer {
                repoList.addAll(it)
                if (it.isNotEmpty()) setViewState(isSuccess = true)
                else setViewState(isError = Throwable("Not found"))
            },
            Consumer {
                setViewState(isError = it)
            })
    }

    fun hideKeyboard() {
        SearchKeyboardUtils.getInstance().hide()
    }
}