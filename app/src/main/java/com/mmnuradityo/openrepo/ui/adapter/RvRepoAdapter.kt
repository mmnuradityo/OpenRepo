package com.mmnuradityo.openrepo.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseRvAdapter
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.databinding.ItemRepoBinding
import com.mmnuradityo.openrepo.utils.ActionClick
import com.mmnuradityo.openrepo.viewmodel.HomeVM

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class RvRepoAdapter(vm: HomeVM) : BaseRvAdapter<HomeVM, GithubRepository>(vm) {

    private val listOfView = mutableListOf<Int>()
    private val REPOSITORY = 1
    private val LOADING = 2

    override fun injectLayout(position: Int): Int = when (listOfView[position]) {
        REPOSITORY -> R.layout.item_repo
        else -> R.layout.item_repo_loading
    }

    override fun setHolder(itemBinding: ViewDataBinding): BaseVH<HomeVM, GithubRepository> =
        when (itemBinding) {
            is ItemRepoBinding -> RepoVH(itemBinding)
            else -> LoadVH(itemBinding)
        }

    override fun onBindViewHolder(holder: BaseVH<HomeVM, GithubRepository>, position: Int) {
        val layoutParams =
            holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = listOfView[position] == LOADING
        holder.itemView.layoutParams = layoutParams

        if (listOfView[position] == REPOSITORY) {
            super.onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int = listOfView.size

    override fun addOrUpdate(data: MutableList<GithubRepository>) {
        if (data.isEmpty()) return
        removeLoading()

        for (index in data.indices) {
            val position = findPosition(data[index])
            if (position == -1) {
                datas.add(data[index])
                listOfView.add(REPOSITORY)
                val lastPosition = itemCount - 1
                notifyItemInserted(lastPosition)
                notifyItemRangeInserted(lastPosition, itemCount)
            } else {
                datas.removeAt(position)
                datas.add(position, data[index])
                notifyItemChanged(position)
                notifyItemRangeChanged(position, itemCount)
            }
        }
        addLoading()
    }

    private fun addLoading() {
        val position = listOfView.size - 1
        if (listOfView[position] != LOADING) {
            listOfView.add(LOADING)
            notifyItemInserted(position)
            notifyItemRangeRemoved(position, itemCount)
        }
    }

    fun firstLoading() {
        val position = listOfView.size
        listOfView.add(LOADING)
        notifyItemInserted(position)
        notifyItemRangeRemoved(position, itemCount)
    }

    fun removeLoading() {
        if (listOfView.size == 0) return

        val position = listOfView.size - 1
        if (listOfView[position] == LOADING) {
            listOfView.remove(listOfView[position])
            notifyItemRemoved(position)
        }
    }

    class RepoVH(private val binding: ItemRepoBinding) : BaseVH<HomeVM, GithubRepository>(binding) {

        override fun bind(viewModel: HomeVM, data: GithubRepository) {
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

    class LoadVH(binding: ViewDataBinding) : BaseVH<HomeVM, GithubRepository>(binding)
}