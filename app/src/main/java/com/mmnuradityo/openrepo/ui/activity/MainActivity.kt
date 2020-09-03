package com.mmnuradityo.openrepo.ui.activity

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.View.DragShadowBuilder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseActivity
import com.mmnuradityo.openrepo.base.obtainVM
import com.mmnuradityo.openrepo.base.replaceFragment
import com.mmnuradityo.openrepo.base.replaceFragmentWithBackStack
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.databinding.ActivityMainBinding
import com.mmnuradityo.openrepo.ui.dialog.ExitDialog
import com.mmnuradityo.openrepo.ui.dialog.LogoutDialog
import com.mmnuradityo.openrepo.ui.dialog.MenuDialog
import com.mmnuradityo.openrepo.ui.dialog.SettingsDialog
import com.mmnuradityo.openrepo.ui.fragment.*
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.utils.search.SearchKeyboardUtils
import com.mmnuradityo.openrepo.utils.search.SearchTextWatcher
import com.mmnuradityo.openrepo.viewmodel.MainVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class MainActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var menuDialog: MenuDialog
    private lateinit var settingsDialog: SettingsDialog
    private lateinit var logoutDialog: LogoutDialog
    private var fragmentSearch: SearchFragment? = null
    private val data = ClipData.newPlainText("", "")
    private var frameId: Int = 0

    companion object {
        fun generateIntent(context: Context) {
            val i = Intent(context, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)
        }
    }

    override fun injectLayout(): Int = R.layout.activity_main

    override fun initComponent(savedInstanceState: Bundle?) {
        viewBinding = getViewBinding() as ActivityMainBinding
        frameId = R.id.frameMain
    }

    override fun initView() {
        viewBinding.vm = obtainVM(MainVM::class.java).apply {
            getAppDetail(this@MainActivity)
        }

        viewBinding.vm?.let {
            if (it.isLogin) getStarted() else changeFragment(LoginFragment.newInstance())

            menuDialog = MenuDialog(this@MainActivity, it)
            settingsDialog = SettingsDialog(this@MainActivity, it)
            logoutDialog = LogoutDialog(this@MainActivity, it)
            SearchKeyboardUtils.init(this, etSearch)
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    override fun listener() {
        viewBinding.vm?.apply {
            viewState.observe(this@MainActivity, Observer { handleState(it) })
        }

        viewBinding.apply {
            onTouch = View.OnTouchListener { view, motionEvent ->
                if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                    val width = view.width * 2
                    val height = view.height * 2
                    dragShadow(view, width, height)
                    true
                } else {
                    false
                }
            }

            onDrag = View.OnDragListener { _, dragEvent ->
                val action = dragEvent.action
                if (action == DragEvent.ACTION_DRAG_ENDED) {
                    val viewChild = dragEvent.localState as View
                    viewChild.visibility = View.VISIBLE
                    menuDialog.show()
                }
                true
            }

            onSearch =
                SearchTextWatcher(this@MainActivity)

            hideSearch = object : ActionClick {
                override fun onClick() {
                    showOrHideSearch(false)
                }
            }

            onActionEnter = TextView.OnEditorActionListener { v, actionId, event ->
                if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER
                    || actionId == EditorInfo.IME_ACTION_DONE
                ) {
                    getSearchFragment()?.onSearch((v as EditText).text.toString())
                }
                false
            }
        }

        menuDialog.behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val fraction = abs(slideOffset) / bottomSheet.scaleX
                if (fraction >= 0.40f) menuDialog.behavior.state = STATE_COLLAPSED
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_COLLAPSED) menuDialog.dismiss()
            }
        })

        settingsDialog.behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val fraction = abs(slideOffset) / bottomSheet.scaleX
                if (fraction >= 0.40f) settingsDialog.behavior.state = STATE_COLLAPSED
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_COLLAPSED) settingsDialog.dismiss()
            }
        })
    }

    private fun handleState(viewState: ViewState) {
        viewState.apply {
            onEvent?.let {
                if (it) {
                    when (viewState.mode) {
                        MainVM.MENU_HOME -> {
                            supportFragmentManager.popBackStack(
                                null,
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                            )

                            viewBinding.vm?.profile?.value?.let { data ->
                                changeFragment(HomeFragment.newInstance(data))
                            }
                        }
                        MainVM.MENU_SEARCH -> showOrHideSearch(true)
                        MainVM.MENU_SETTINGS -> settingsDialog.show()
                        else -> {
                            viewBinding.vm?.logout()
                            changeFragment(LoginFragment.newInstance())
                        }
                    }

                    if (viewState.mode != MainVM.MENU_SEARCH && fragmentSearch == null) {
                        showOrHideSearch(false)
                    }
                }
            }

            onLoad?.let { if (it) changeFragment(LoadingFragment.newInstance()) }

            onSuccess?.let {
                if (it) viewBinding.vm?.profile?.value?.let { data ->
                    Handler(Looper.getMainLooper()).postDelayed({
                        changeFragment(HomeFragment.newInstance(data))
                    }, 3000)
                }
            }

            onError?.let { onError(it) }
        }
    }

    private fun showOrHideSearch(isShow: Boolean) {
        if (!isShow) {
            adjustFrameSearch(isShow)
            fragmentSearch = null
            viewBinding.etSearch.text.clear()
        } else {
            adjustFrameSearch(!isShow)
        }

        var isHide: Boolean
        viewBinding.apply {
            isHide = !frameSearch.isVisible

            val delay: Long
            val anim = if (isHide) {
                delay = 0
                R.anim.slide_down
            } else {
                SearchKeyboardUtils.getInstance().hide()
                delay = 800
                R.anim.slide_up_search
            }

            etSearch.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, anim))
            Handler(Looper.getMainLooper()).postDelayed({ showSearch(isShow) }, delay)
        }
    }

    fun adjustFrameSearch(isSearch: Boolean) {
        viewBinding.frameSearch.layoutParams.height =
            if (isSearch) ViewGroup.LayoutParams.WRAP_CONTENT
            else ViewGroup.LayoutParams.MATCH_PARENT
    }

    fun getStarted() {
        viewBinding.vm?.start()
    }

    private fun changeFragment(fragment: Fragment) {
        fragment::class.simpleName?.let {
            val isNotLogin = !it.contains("LoginFragment")
            val isNotLoading = !it.contains("LoadingFragment")

            viewBinding.vm?.apply {
                startAnim.set(
                    if (isNotLogin && isNotLoading)
                        AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_up)
                    else null
                )
            }
        }

        replaceFragment(frameId, fragment)
    }

    fun changeSearchFragment() {
        val fragment = getFragment(SearchFragment::class.simpleName)

        if (fragment == null) {
            fragmentSearch = SearchFragment.newInstance()
            fragmentSearch?.let {
                replaceFragmentWithBackStack(getFrameFragment(), it)
            }
        }
    }

    fun getFrameFragment() = frameId

    fun getSearchFragment() = fragmentSearch

    private fun getFragment(fragmentTag: String?) =
        supportFragmentManager.findFragmentByTag(fragmentTag)

    fun showSearch(isShow: Boolean) {
        viewBinding.frameSearch.visibility = (if (isShow) {
            SearchKeyboardUtils.getInstance().show()
            View.VISIBLE
        } else View.GONE)

    }

    override fun onBackPressed() {
        if (getFragment(RepositoryFragment::class.simpleName) == null)
            showOrHideSearch(false)

        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            showDialogExit()
        }
    }

    private fun dragShadow(view: View?, width: Int, height: Int) {
        val scale = 2f
        val shadowBuilder = object : DragShadowBuilder(view) {

            override fun onProvideShadowMetrics(
                outShadowSize: Point?,
                outShadowTouchPoint: Point?
            ) {
                outShadowSize?.set(width, height)
                outShadowTouchPoint?.set(width / 2, height / 2)
            }

            override fun onDrawShadow(canvas: Canvas) {
                canvas.scale(scale, scale)
                getView().draw(canvas)
            }
        }

        view?.apply {
            startDragAndDrop(data, shadowBuilder, view, View.DRAG_FLAG_OPAQUE)
            visibility = View.INVISIBLE
        }
    }

    fun showDialogExit() {
        ExitDialog(this).show()
    }

    fun showDialogLogout() {
        logoutDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.vm?.finish()
    }

    object MainBinding {
        @JvmStatic
        @BindingAdapter("app:startAnim")
        fun onShowDragBtn(v: View, anim: Animation?) {
            v.visibility = View.GONE

            anim?.let {
                Handler(Looper.getMainLooper()).postDelayed({
                    v.startAnimation(it)
                    v.visibility = View.VISIBLE
                }, 1000)
            }
        }

        @JvmStatic
        @BindingAdapter("app:touchShowMenu")
        fun onShowMenu(v: View, listener: View.OnTouchListener?) {
            v.setOnTouchListener(listener)
        }

        @JvmStatic
        @BindingAdapter("app:dragShowMenu")
        fun onDrag(v: View, listener: View.OnDragListener?) {
            v.setOnDragListener(listener)
        }

        @JvmStatic
        @BindingAdapter("app:searchRepo")
        fun onSearch(v: EditText, listener: SearchTextWatcher?) {
            v.addTextChangedListener(listener)
        }

        @JvmStatic
        @BindingAdapter("app:editorActionListener")
        fun onEditorActionListener(v: EditText, listener: TextView.OnEditorActionListener?) {
            v.setOnEditorActionListener(listener)
        }
    }

}
