package com.mmnuradityo.openrepo.utils.search

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import com.mmnuradityo.openrepo.ui.activity.MainActivity
import com.mmnuradityo.openrepo.ui.fragment.RepositoryFragment

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class SearchTextWatcher(val activity: MainActivity) : TextWatcher {
    private var runnable: Runnable? = null
    private var handler = Handler(Looper.getMainLooper())
    private val fm = activity.supportFragmentManager

    override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {
        char?.length?.let { textSize ->
            if (textSize > 0) {
                fm.apply {
                    val tag = RepositoryFragment::class.simpleName
                    findFragmentByTag(tag)?.let {
                        for (i in 0 until backStackEntryCount) {
                            if (getBackStackEntryAt(i).name == tag) popBackStack()
                        }
                        beginTransaction().remove(it).commit()
                    }
                }
                if (activity.getSearchFragment() == null) activity.changeSearchFragment()
            }
        }
    }

    override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
        char?.length?.let { textSize ->
            if (textSize >= 3) {
                runnable?.let { run -> handler.removeCallbacks(run) }
                runnable = null

                runnable = Runnable {
                    activity.getSearchFragment()?.onSearch(char.toString())
                }
            }
        }
    }

    override fun afterTextChanged(edit: Editable?) {
        edit?.length?.let { textSize ->
            if (textSize >= 3) runnable?.let { handler.postDelayed(it, 1000) }
        }
    }

}
