package com.mmnuradityo.openrepo.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseFragment
import com.mmnuradityo.openrepo.base.obtainVM
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.databinding.FragmentLoginBinding
import com.mmnuradityo.openrepo.ui.activity.MainActivity
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.utils.Constant
import com.mmnuradityo.openrepo.viewmodel.LoginVM

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class LoginFragment : BaseFragment<LoginFragment>() {

    private lateinit var viewBinding: FragmentLoginBinding

    companion object {
        fun newInstance() = generateInstance(LoginFragment())
    }

    override fun initComponent(context: Context) {
    }

    override fun initViewComponent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        viewBinding.vm = getComponent().obtainVM(LoginVM::class.java).apply {
            isBtnActive.set(true)
        }
    }

    override fun listener() {
        viewBinding.apply {
            vm?.viewState?.observe(this@LoginFragment, Observer {
                handlerState(it)
            })

            loginClick = object : ActionClick {
                override fun onClick() {
                    vm?.login(userName.text.toString(), password.text.toString())
                }
            }

            forgotClick = object : ActionClick {
                override fun onClick() {
                    onOpenBrowser(Constant.FORGOT_PASS_URL)
                }
            }

            createdClick = object : ActionClick {
                override fun onClick() {
                    onOpenBrowser(Constant.SIGN_UP_URL)
                }
            }
        }
    }

    private fun handlerState(state: ViewState?) {
        state?.onLoad?.let {
            if (it) {
                setProgress(R.drawable.load_loading_progress)
            } else {
                setProgress(R.drawable.ic_openrepo)
            }
        }

        state?.onSuccess?.let { if (it) (activity as MainActivity).getStarted() }

        state?.onError?.let { onError(it) }
    }

    private fun setProgress(drawable: Int) {
        activity?.let {
            viewBinding.progress.apply {
                val drawProgress = ContextCompat.getDrawable(it, drawable)
                progressDrawable = drawProgress
                indeterminateDrawable = drawProgress
            }
        }
    }

    fun onOpenBrowser(url: String) {
        activity?.let {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            builder.setToolbarColor(ContextCompat.getColor(it, R.color.colorPrimary))
            customTabsIntent.launchUrl(it, Uri.parse(url))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.vm?.finish()
    }

}
