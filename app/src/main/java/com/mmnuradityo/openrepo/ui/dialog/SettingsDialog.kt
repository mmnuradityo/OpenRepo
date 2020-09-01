package com.mmnuradityo.openrepo.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mmnuradityo.openrepo.OpenRepoApp
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.databinding.BottomSheetSettingsBinding
import com.mmnuradityo.openrepo.ui.activity.MainActivity
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.viewmodel.MainVM

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class SettingsDialog(private val activity: Activity, private val vm: MainVM) :
    BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme) {

    private lateinit var binding: BottomSheetSettingsBinding

    init {
        setup()
        initView()
        listener()
    }

    @SuppressLint("InflateParams")
    private fun setup() {
        val root = layoutInflater.inflate(R.layout.bottom_sheet_settings, null)
        binding = BottomSheetSettingsBinding.inflate(layoutInflater, root as ViewGroup, false)
        binding.vm = vm
        setContentView(binding.root)
        window?.setDimAmount(0f)
        binding.executePendingBindings()
    }

    private fun initView() {
        binding.apply {
            vm?.let {
                switchDarkMode.isChecked = it.isDarkMode
            }
        }
    }

    private fun listener() {
        binding.apply {

            darkModeClick = object : ActionClick {
                override fun onClick() {
                    val isDarkMode = switchDarkMode.isChecked
                    vm?.isDarkMode = isDarkMode
                    OpenRepoApp.getInstance().setDarkMode(isDarkMode)
                }
            }

            logoutClick = object : ActionClick {
                override fun onClick() {
                    (activity as MainActivity).showDialogLogout()
                    dismiss()
                }
            }

        }
    }

}