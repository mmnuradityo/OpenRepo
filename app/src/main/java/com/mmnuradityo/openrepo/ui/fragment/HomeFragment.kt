package com.mmnuradityo.openrepo.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseActivity
import com.mmnuradityo.openrepo.base.BaseFragment
import com.mmnuradityo.openrepo.base.obtainVM
import com.mmnuradityo.openrepo.base.replaceFragmentWithBackStack
import com.mmnuradityo.openrepo.data.model.GithubProfile
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.databinding.FragmentHomeBinding
import com.mmnuradityo.openrepo.ui.activity.MainActivity
import com.mmnuradityo.openrepo.ui.adapter.RvRepoAdapter
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.utils.RvEndlessListener
import com.mmnuradityo.openrepo.viewmodel.HomeVM
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlin.math.abs

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class HomeFragment : BaseFragment<HomeFragment>() {

    private lateinit var viewBinding: FragmentHomeBinding
    private var dataProfile: GithubProfile? = null

    companion object {
        private const val DATA_PROFILE = "data_profile"

        fun newInstance(profile: GithubProfile?): HomeFragment {
            val bundle = Bundle()
            bundle.putParcelable(DATA_PROFILE, profile)
            return generateInstance(HomeFragment())
        }
    }

    override fun initComponent(savedInstanceState: Bundle?) {
        dataProfile = arguments?.getParcelable(DATA_PROFILE)
    }

    override fun initViewComponent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        viewBinding.vm = (activity as BaseActivity).obtainVM(HomeVM::class.java).apply {
            setupLoadDataSize(10)

            viewBinding.rvRepos.adapter = RvRepoAdapter(this)
            (viewBinding.rvRepos.adapter as RvRepoAdapter).firstLoading()

            if (dataProfile == null) {
                start()
            } else {
                profile.set(dataProfile)
            }
        }
    }

    override fun listener() {
        viewBinding.vm?.apply {
            viewState.observe(this@HomeFragment, Observer { handelState(it) })

            activity?.let {
                viewBinding.drag = AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
                    appBarSlider(it, appBarLayout, offset)
                }

                openRepo.observe(it, Observer { onRepoClicked(it) })
            }
        }

        viewBinding.apply {
            activity?.let {
                followerClick = object : ActionClick {
                    override fun onClick() {
                        (it as BaseActivity).replaceFragmentWithBackStack(
                            (it as MainActivity).getFrameFragment(),
                            FollowFragment.newInstance(FollowFragment.FOLLOWERS)
                        )
                    }
                }

                followingClick = object : ActionClick {
                    override fun onClick() {
                        (it as BaseActivity).replaceFragmentWithBackStack(
                            (it as MainActivity).getFrameFragment(),
                            FollowFragment.newInstance(FollowFragment.FOLLOWING)
                        )
                    }
                }
            }

            repoClick = object : ActionClick {
                override fun onClick() {
                    viewBinding.appbar.setExpanded(false)
                }
            }
        }

    }

    private fun appBarSlider(context: Context, appBarLayout: AppBarLayout, offset: Int) {
        viewBinding.appbarSlider.apply {
            val fraction = abs(offset).toFloat() / appBarLayout.totalScrollRange
            val anim = AnimationUtils.loadAnimation(context, R.anim.slide_down)

            if (visibility == View.GONE) {
                if (fraction == 1f) {
                    startAnimation(anim)
                    visibility = View.VISIBLE
                }
            } else if (fraction != 1f) {
                visibility = View.GONE
            }
        }
    }

    private fun handelState(state: ViewState) {
        onError(state.onError)
    }

    private fun onRepoClicked(data: GithubRepository) {
        (activity as MainActivity).replaceFragmentWithBackStack(
            (activity as MainActivity).getFrameFragment(),
            RepositoryFragment.newInstance(data)
        )
    }

    override fun onResume() {
        super.onResume()
        viewBinding.vm?.getRepoData(1)
    }

    object HomeBinding {
        @JvmStatic
        @BindingAdapter("app:dragListener")
        fun onDrag(appbar: AppBarLayout, listener: AppBarLayout.OnOffsetChangedListener) {
            val behavior = AppBarLayout.Behavior()
            behavior.setDragCallback(object : DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return true
                }
            })

            (appbar.layoutParams as CoordinatorLayout.LayoutParams).behavior = behavior
            appbar.addOnOffsetChangedListener(listener)
        }

        @JvmStatic
        @BindingAdapter("app:repoList")
        fun setRepoList(rv: RecyclerView, repoData: MutableList<GithubRepository>) {
            with(rv.adapter as RvRepoAdapter) {
                addOrUpdate(repoData)
            }
        }

        @JvmStatic
        @BindingAdapter("app:repoScrollListener")
        fun repoScrollListener(rv: RecyclerView, vm: HomeVM) {
            rv.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            rv.addOnScrollListener(RvEndlessListener(rv.layoutManager) {
                Handler(Looper.getMainLooper()).postDelayed({
                    vm.getRepoData(it + 1)
                }, 1000)
            })
        }

        @JvmStatic
        @BindingAdapter("app:repoLoading")
        fun onLoading(rv: RecyclerView, isActive: Boolean?) {
            isActive?.let { if (!it) (rv.adapter as RvRepoAdapter).removeLoading() }
        }
    }

}
