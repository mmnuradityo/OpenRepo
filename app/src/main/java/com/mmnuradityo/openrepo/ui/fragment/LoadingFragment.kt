package com.mmnuradityo.openrepo.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mmnuradityo.openrepo.base.BaseFragment
import com.mmnuradityo.openrepo.databinding.FragmentLoadingBinding

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class LoadingFragment : BaseFragment<LoadingFragment>() {

    companion object {
        private const val DATA_KEY = "data_key"

        fun newInstance(): LoadingFragment {
            val bundle = Bundle()
            val fragment = LoadingFragment()
            bundle.putString(DATA_KEY, fragment::class.simpleName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewBinding: FragmentLoadingBinding

    override fun initComponent(context: Context) {
    }

    override fun initViewComponent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoadingBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        with(viewBinding) {
            val handler = Handler(Looper.getMainLooper())
            try {
                val runnable: Runnable = object : Runnable {
                    var count = 0

                    @SuppressLint("SetTextI18n")
                    override fun run() {
                        count++
                        textLoading = when (count) {
                            1 -> "Loading . "
                            2 -> "Loading . . "
                            3 -> "Loading . . ."
                            else -> "Loading "
                        }
                        if (count == 4) count = 0
                        handler.postDelayed(this, 2 * 300.toLong())
                    }
                }

                handler.postDelayed(runnable, 1 * 300.toLong())

            } catch (e: Exception) {
                e.printStackTrace()
            }

            executePendingBindings()
        }
    }

    override fun listener() {
    }
}