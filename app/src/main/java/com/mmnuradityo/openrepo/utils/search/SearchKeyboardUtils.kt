package com.mmnuradityo.openrepo.utils.search

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mmnuradityo.openrepo.data.repository.Repository

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class SearchKeyboardUtils private constructor(val activity: Activity, val view: View) {

    private val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    companion object {
        private var INSTANCE: SearchKeyboardUtils? = null

        @JvmStatic
        fun init(activity: Activity, view: View) =
            INSTANCE
                ?: synchronized(Repository::class.java) {
                INSTANCE
                    ?: SearchKeyboardUtils(
                        activity,
                        view
                    )
                        .also { INSTANCE = it }
            }

        @JvmStatic
        fun getInstance() = INSTANCE!!
    }

    fun hide() {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun show() {
        if (!view.isFocused) {
            view.requestFocus()
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}