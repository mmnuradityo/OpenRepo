package com.mmnuradityo.openrepo.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.databinding.BottomSheetMenuBinding
import com.mmnuradityo.openrepo.ui.activity.MainActivity
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.viewmodel.MainVM

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class MenuDialog(val activity: Activity, private val vm: MainVM) :
    BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme) {

    private lateinit var binding: BottomSheetMenuBinding

    init {
        setup()
        listener()
    }

    @SuppressLint("InflateParams")
    private fun setup() {
        val root = layoutInflater.inflate(R.layout.bottom_sheet_menu, null)
        binding = BottomSheetMenuBinding.inflate(layoutInflater, root as ViewGroup, false)
        setContentView(binding.root)
        window?.setDimAmount(0f)
        binding.executePendingBindings()
    }

    private fun listener() {
        binding.apply {
            menuClick = object : ActionClick {
                override fun onClick() {
                    vm.setViewState(MainVM.MENU_HOME, onEvent = true)
                    dismiss()
                }
            }

            searchClick = object : ActionClick {
                override fun onClick() {
                    vm.setViewState(MainVM.MENU_SEARCH, onEvent = true)
                    dismiss()
                }
            }

            settingClick = object : ActionClick {
                override fun onClick() {
                    vm.setViewState(MainVM.MENU_SETTINGS, onEvent = true)
                    dismiss()
                }
            }

            exitClick = object : ActionClick {
                override fun onClick() {
                    (activity as MainActivity).showDialogExit()
                    dismiss()
                }
            }
        }
    }

}