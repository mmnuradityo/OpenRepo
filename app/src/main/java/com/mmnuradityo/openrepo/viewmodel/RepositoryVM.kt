package com.mmnuradityo.openrepo.viewmodel

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.databinding.ObservableField
import com.mmnuradityo.openrepo.base.BaseVM
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.data.viewstate.ViewState

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class RepositoryVM(application: Application) :
    BaseVM(application, ViewState()) {

    val data: ObservableField<GithubRepository> = ObservableField()
    val created: ObservableField<String> = ObservableField()
    val updated: ObservableField<String> = ObservableField()
    val pushed: ObservableField<String> = ObservableField()

    override fun start() {
        data.get()?.let {
            created.set(getTime(it.createdAt))
            updated.set(getTime(it.updatedAt))
            pushed.set(getTime(it.pushedAt))
        }
    }

    private fun getTime(dateTime: String?): String {
        val defaultParse = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        defaultParse.timeZone = TimeZone.getTimeZone("UTC")
        val date = defaultParse.parse(dateTime)
        val parser = SimpleDateFormat("dd/MM/YYYY")
        return parser.format(date)
    }
}