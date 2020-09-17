package com.mmnuradityo.openrepo.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseFragment
import com.mmnuradityo.openrepo.base.obtainVM
import com.mmnuradityo.openrepo.data.model.follower.Follower
import com.mmnuradityo.openrepo.databinding.FragmentFollowBinding
import com.mmnuradityo.openrepo.ui.adapter.RvFollowersAdapter
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.viewmodel.FollowerVM
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class FollowFragment : BaseFragment<FollowFragment>() {

    private lateinit var viewBinding: FragmentFollowBinding
    private var dataMode: Int? = 0

    companion object {
        const val MODE = "mode"
        const val FOLLOWERS = 1
        const val FOLLOWING = 2

        fun newInstance(mode: Int): FollowFragment {
            val bundle = Bundle()
            bundle.putInt(MODE, mode)
            return generateInstance(FollowFragment(), bundle)
        }
    }

    override fun initComponent(context: Context) {
        dataMode = arguments?.getInt(MODE, 0)
    }

    override fun initViewComponent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentFollowBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        viewBinding.vm = getComponent().obtainVM(FollowerVM::class.java).apply {
            activity?.let {
                viewBinding.rvFollowers.adapter = RvFollowersAdapter(it, this)
                viewBinding.rvFollowing.adapter = RvFollowersAdapter(it, this)
            }
            start()
        }

        dataMode?.let {
            viewBinding.apply {
                when (it) {
                    FOLLOWERS -> showOrHideList(FOLLOWERS)
                    else -> showOrHideList(FOLLOWING)
                }
            }
        }
    }

    override fun listener() {
        viewBinding.apply {
            vm?.viewState?.observe(this@FollowFragment, Observer { onError(it.onError) })

            followersClick = object : ActionClick {
                override fun onClick() {
                    showOrHideList(FOLLOWERS)
                }
            }

            followingClick = object : ActionClick {
                override fun onClick() {
                    showOrHideList(FOLLOWING)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.vm?.finish()
    }

    private fun showOrHideList(mode: Int) {
        viewBinding.apply {
            if (mode == FOLLOWERS) {
                val rotateFollowers = if (ivFollowers.rotation >= 180f) 0f else 180f
                ivFollowers.animate().rotation(rotateFollowers).start()
                ivFollowing.animate().rotation(0f).start()

                rvFollowing.visibility = View.GONE
                when (rvFollowers.isVisible) {
                    true -> rvFollowers.visibility = View.GONE
                    else -> {
                        rvFollowers.startAnimation(
                            AnimationUtils.loadAnimation(activity, R.anim.slide_down)
                        )
                        rvFollowers.visibility = View.VISIBLE
                    }
                }

            } else {
                val rotateFollowing = if (ivFollowing.rotation >= 180f) 0f else 180f
                ivFollowing.animate().rotation(rotateFollowing).start()
                ivFollowers.animate().rotation(0f).start()

                rvFollowers.visibility = View.GONE
                when (rvFollowing.isVisible) {
                    true -> rvFollowing.visibility = View.GONE
                    else -> {
                        rvFollowing.startAnimation(
                            AnimationUtils.loadAnimation(activity, R.anim.slide_down)
                        )
                        rvFollowing.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    object FollowerBinding {
        @JvmStatic
        @BindingAdapter("app:followersList")
        fun setFollowers(rv: RecyclerView, datas: MutableList<Follower>) {
            rv.itemAnimator = SlideInDownAnimator()
            with(rv.adapter as RvFollowersAdapter) {
                addOrUpdate(datas)
            }
        }

        @JvmStatic
        @BindingAdapter("app:followingList")
        fun setFollowing(rv: RecyclerView, datas: MutableList<Follower>) {
            rv.itemAnimator = SlideInDownAnimator()
            with(rv.adapter as RvFollowersAdapter) {
                addOrUpdate(datas)
            }
        }
    }

}