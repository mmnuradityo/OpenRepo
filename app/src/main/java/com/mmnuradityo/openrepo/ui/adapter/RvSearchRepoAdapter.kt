package com.mmnuradityo.openrepo.ui.adapter

import androidx.databinding.ViewDataBinding
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseRvAdapter
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.databinding.ItemRepoBinding
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.viewmodel.SearchVM

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class RvSearchRepoAdapter(vm: SearchVM) : BaseRvAdapter<SearchVM, GithubRepository>(vm) {

    override fun injectLayout(position: Int): Int = R.layout.item_repo

    override fun setHolder(itemBinding: ViewDataBinding): BaseVH<SearchVM, GithubRepository> =
        SearchRepoVH(itemBinding as ItemRepoBinding)

    class SearchRepoVH(private val binding: ItemRepoBinding) : BaseVH<SearchVM, GithubRepository>(binding) {

        override fun bind(viewModel: SearchVM, data: GithubRepository) {
            binding.datas = data
            binding.repoClick = object : ActionClick {
                override fun onClick() {
                    viewModel.openRepo.value = data
                    viewModel.hideKeyboard()
                }
            }
            binding.executePendingBindings()
        }
    }

}