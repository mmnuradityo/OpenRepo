package com.mmnuradityo.openrepo.viewmodel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.mmnuradityo.openrepo.base.BaseVM
import com.mmnuradityo.openrepo.data.model.follower.Follower
import com.mmnuradityo.openrepo.data.repository.Repository
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import io.reactivex.rxjava3.functions.Consumer

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class FollowerVM(application: Application, private val repository: Repository) :
    BaseVM(application, ViewState()) {

    val followerList: ObservableList<Follower> = ObservableArrayList()
    val followingList: ObservableList<Follower> = ObservableArrayList()

    override fun start() {
        super.start()
        getFollowers()
        getFollowing()
    }

    private fun getFollowers() {
        repository.getFollowers(Consumer {
            it?.let {
                followerList.addAll(it)
                setViewState(isSuccess = true)
            }
        }, Consumer {
            setViewState(isError = it)
        })
    }

    private fun getFollowing() {
        repository.getFollowing(Consumer {
            it?.let {
                followingList.addAll(it)
                setViewState(isSuccess = true)
            }
        }, Consumer {
            setViewState(isError = it)
        })
    }
}