package com.mmnuradityo.openrepo.base

import android.app.Application
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.utils.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
abstract class BaseVM(application: Application, private val viewStateCondition: ViewState) :
    AndroidViewModel(application), BaseComponent.ViewModel {

    private val stateCondition = SingleLiveEvent<ViewState>().apply { value = viewStateCondition }
    val viewState: LiveData<ViewState> get() = stateCondition
    private val disposable = CompositeDisposable()

    override fun start() {
        setViewState(isLoad = true)
    }

    override fun setViewState(mode: Int?, onEvent: Boolean?, isLoad: Boolean?, isSuccess: Boolean?, isError: Throwable?) {
        this.stateCondition.value = stateCondition.value?.copy(
            mode = mode, onEvent = onEvent, onLoad = isLoad, onSuccess = isSuccess, onError = isError
        )
    }

    fun <T> compare(firstData: ObservableList<T>, secondData: List<T>) {
        if (secondData.isEmpty()) return
        if (firstData.isEmpty() && secondData.isNotEmpty()) {
            firstData.addAll(secondData)
        } else {
            for (item in secondData) {
                if (!firstData.contains(item)) {
                    firstData.add(item)
                }
            }
        }
    }

    override fun addDisposable(dispose: Disposable) {
        disposable.add(dispose)
    }

    override fun finish() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}