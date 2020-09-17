package com.mmnuradityo.openrepo.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseFragment
import com.mmnuradityo.openrepo.base.obtainVM
import com.mmnuradityo.openrepo.base.replaceFragmentWithBackStack
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.data.viewstate.ViewState
import com.mmnuradityo.openrepo.databinding.FragmentSearchBinding
import com.mmnuradityo.openrepo.ui.activity.MainActivity
import com.mmnuradityo.openrepo.ui.adapter.RvSearchRepoAdapter
import com.mmnuradityo.openrepo.viewmodel.SearchVM
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class SearchFragment : BaseFragment<SearchFragment>() {

    private lateinit var viewBinding: FragmentSearchBinding

    companion object {
        fun newInstance() = generateInstance(SearchFragment())
    }

    override fun initComponent(context: Context) {
    }

    override fun initViewComponent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).adjustFrameSearch(true)

        viewBinding.vm = getComponent().obtainVM(SearchVM::class.java).apply {
            viewBinding.rvReposSearch.adapter = RvSearchRepoAdapter(this)
        }
    }

    override fun listener() {
        viewBinding.vm?.apply {
            viewState.observe(this@SearchFragment, Observer { it?.let { handleState(it) } })
            openRepo.observe(this@SearchFragment, Observer { onRepoClicked(it) })
        }
    }

    private fun handleState(viewState: ViewState) {
        viewState.apply {
            onLoad?.let { viewBinding.loading.visibility = if (it) View.VISIBLE else View.GONE }

            onError?.let { viewBinding.vm?.textRepo?.set(resources.getString(R.string.repo_not_found)) }
        }
    }

    fun onSearch(repoName: String) {
        activity?.let {
            viewBinding.vm?.apply {
                textRepo.set(resources.getString(R.string.repo_search))
                getRepoData(repoName)
            }
        }
    }

    override fun onResume() {
        (activity as MainActivity).showSearch(true)
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.vm?.repoList?.clear()
    }

    private fun onRepoClicked(data: GithubRepository) {
        (activity as MainActivity).replaceFragmentWithBackStack(
            (activity as MainActivity).getFrameFragment(),
            RepositoryFragment.newInstance(data)
        )
    }

    object SearchBinding {
        @JvmStatic
        @BindingAdapter("app:repoSearchList")
        fun setRepoList(rv: RecyclerView, repoData: MutableList<GithubRepository>) {
            rv.visibility = if (repoData.size > 0) View.VISIBLE else View.GONE
            rv.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            with(rv.adapter as RvSearchRepoAdapter) {
                clearList()
                Handler(Looper.getMainLooper()).postDelayed({ addOrUpdate(repoData) }, 300)
            }
        }
    }

}