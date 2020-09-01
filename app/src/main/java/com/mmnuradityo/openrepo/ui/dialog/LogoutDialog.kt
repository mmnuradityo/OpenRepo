package com.mmnuradityo.openrepo.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.utils.showText
import com.mmnuradityo.openrepo.viewmodel.MainVM
import kotlinx.android.synthetic.main.dialog_exit.*

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class LogoutDialog(private val activity: Activity, private val vm: MainVM) : Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_logout)
        window?.apply {
            setBackgroundDrawable(null)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setCancelable(false)
        }

        btnNo.setOnClickListener {
            dismiss()
        }

        btnYes.setOnClickListener {
            vm.setViewState(MainVM.MENU_LOGOUT, onEvent = true)
            activity.showText("Logout success")
            dismiss()
        }
    }

}