package com.mmnuradityo.openrepo.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import com.mmnuradityo.openrepo.base.BaseVM
import com.mmnuradityo.openrepo.data.repository.Repository
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.utils.LoginUtils
import io.reactivex.rxjava3.functions.Consumer

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class LoginVM(application: Application, private val repository: Repository) :
    BaseVM(application, ViewState()) {

    val isBtnActive: ObservableField<Boolean> = ObservableField()

    fun login(userName: String, password: String) {
        val text = LoginUtils.validate(userName, password)

        if (text == LoginUtils.VALID) {
            setViewState(isLoad = true)
            isBtnActive.set(false)

            addDisposable(
                repository.login(userName, password,
                    Consumer {
                        if (it != null) {
                            login(userName)
                            setViewState(isSuccess = true)
                        } else {
                            login("")
                            throw Throwable("Something wrong!")
                        }
                    },
                    Consumer {
                        login("")
                        setViewState(isError = it)
                    })
            )
        } else {
            login("")
            setViewState(isError = Throwable(text))
        }
    }

    private fun login(name: String) {
        repository.apply {
            if (name != "") {
                isLogin = true
                userName = name
            } else {
                isBtnActive.set(true)
                isLogin = false
                userName = name
            }
        }
    }

}