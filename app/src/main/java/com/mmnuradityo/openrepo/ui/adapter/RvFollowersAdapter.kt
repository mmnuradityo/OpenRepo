package com.mmnuradityo.openrepo.ui.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseRvAdapter
import com.mmnuradityo.openrepo.data.model.follower.Follower
import com.mmnuradityo.openrepo.databinding.ItemFollowerBinding
import com.mmnuradityo.openrepo.viewmodel.FollowerVM

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class RvFollowersAdapter(private val context: Context, vm: FollowerVM) :
    BaseRvAdapter<FollowerVM, Follower>(vm) {

    override fun injectLayout(position: Int): Int = R.layout.item_follower

    override fun setHolder(itemBinding: ViewDataBinding): BaseVH<FollowerVM, Follower> =
        FollowersHolder(itemBinding as ItemFollowerBinding)

    class FollowersHolder(private val viewBinding: ItemFollowerBinding) :
        BaseVH<FollowerVM, Follower>(viewBinding) {

        override fun bind(viewModel: FollowerVM, data: Follower) {
            viewBinding.data = data
            viewBinding.executePendingBindings()
        }
    }

}