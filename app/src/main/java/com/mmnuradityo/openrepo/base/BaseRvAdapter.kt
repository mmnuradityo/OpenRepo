package com.mmnuradityo.openrepo.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.mmnuradityo.openrepo.base.BaseRvAdapter.BaseVH

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
abstract class BaseRvAdapter<VM : ViewModel, T>(private val viewModel: VM) :
    RecyclerView.Adapter<BaseVH<VM, T>>(), BaseComponent.RvAdapter<T, BaseVH<VM, T>> {

    var datas = mutableListOf<T>()

    override fun getItemViewType(position: Int): Int {
        return injectLayout(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<VM, T> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), viewType, parent, false
        )
        return setHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseVH<VM, T>, position: Int) {
        if (position < datas.size) holder.bind(viewModel, datas[position])
    }

    override fun getItemCount(): Int = datas.size

    override fun addOrUpdate(data: MutableList<T>) {
        for (index in data.indices) {
            val position = findPosition(data[index])
            if (position == -1) {
                datas.add(data[index])
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
    }

    override fun addOrUpdate(data: T) {
        val position = findPosition(data)
        if (position == -1) {
            datas.add(data)
            val lastPosition = itemCount - 1
            notifyItemInserted(lastPosition)
            notifyItemRangeInserted(lastPosition, itemCount)
        } else {
            datas.removeAt(position)
            datas.add(position, data)
            notifyItemChanged(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun findPosition(item: T): Int {
        for (index in datas.indices) {
            if (datas[index] == item) {
                return index
            }
        }
        return -1
    }

    override fun remove(position: Int): Boolean {
        if (position < itemCount) {
            datas.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeRemoved(position, itemCount)
            return true
        }
        return false
    }

    override fun clearList() {
        if (datas.isNotEmpty()) {
            datas.clear()
            notifyDataSetChanged()
        }
    }

    open class BaseVH<VM : ViewModel, T>(itemBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root), BaseComponent.RVHolder<VM, T> {

        override fun bind(viewModel: VM, data: T) {
        }
    }

}