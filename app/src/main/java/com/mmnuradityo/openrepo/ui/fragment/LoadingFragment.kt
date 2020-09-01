package com.mmnuradityo.openrepo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mmnuradityo.openrepo.R
import kotlinx.android.synthetic.main.fragment_loading.*

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class LoadingFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_loading, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler(Looper.getMainLooper())
        try {
            val runnable: Runnable = object : Runnable {
                var count = 0
                @SuppressLint("SetTextI18n")
                override fun run() {
                    count++
                    tvLoading?.text = when (count) {
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

    }
}