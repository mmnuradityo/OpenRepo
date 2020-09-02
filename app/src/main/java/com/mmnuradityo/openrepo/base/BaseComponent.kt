package com.mmnuradityo.openrepo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class BaseComponent {

    interface Activity : Component {
        fun injectLayout(): Int

        fun initView()
    }

    interface Fragment : Component {
        fun initViewComponent(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View?

        fun initView(view: View, savedInstanceState: Bundle?)

        fun getComponent(): BaseActivity
    }

    interface Component {
        fun initComponent(savedInstanceState: Bundle?)

        fun listener()

        fun onError(throwable: Throwable?)
    }

    interface RvAdapter<T, VH> {
        fun injectLayout(position: Int): Int

        fun setHolder(itemBinding: ViewDataBinding): VH

        fun addOrUpdate(data: T)

        fun addOrUpdate(data: MutableList<T>)

        fun findPosition(item: T) : Int

        fun remove(position: Int): Boolean

        fun clearList()
    }

    interface RVHolder<VM : androidx.lifecycle.ViewModel, T> {
        fun bind(viewModel: VM, data: T)
    }

    interface ViewModel {
        fun start()

        fun setViewState(
            mode: Int? = 0,
            onEvent: Boolean? = false,
            isLoad: Boolean? = false,
            isSuccess: Boolean? = false,
            isError: Throwable? = null
        )
    }

}