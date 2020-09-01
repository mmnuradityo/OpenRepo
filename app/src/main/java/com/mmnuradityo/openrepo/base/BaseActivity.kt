package com.mmnuradityo.openrepo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mmnuradityo.openrepo.utils.showText

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
abstract class BaseActivity : AppCompatActivity(), BaseComponent.Activity {

    private lateinit var viewBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, injectLayout())
        initComponent(savedInstanceState)
        initView()
        listener()
    }

    fun getViewBinding() = viewBinding

    override fun onError(throwable: Throwable?) {
        throwable?.message?.let { showText(it) }
    }
}