package com.mmnuradityo.openrepo.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mmnuradityo.openrepo.utils.showText

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
abstract class BaseFragment<T : Fragment> : Fragment(), BaseComponent.Fragment {

    private var baseComponent: BaseActivity? = null

    companion object {
        private const val DATA_KEY = "data_key"

        fun <T : Fragment> generateInstance(fragment: T): T =
            generateInstance(fragment, Bundle())

        fun <T : Fragment> generateInstance(fragment: T, bundle: Bundle): T {
            bundle.putString(DATA_KEY, fragment::class.simpleName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseComponent = activity as BaseActivity
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initComponent(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initViewComponent(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listener()
    }

    override fun getComponent(): BaseActivity = baseComponent!!

    override fun onError(throwable: Throwable?) {
        throwable?.message?.let { message -> activity?.showText(message) }
    }

}