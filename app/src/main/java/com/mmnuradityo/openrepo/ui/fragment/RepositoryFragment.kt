package com.mmnuradityo.openrepo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.base.BaseFragment
import com.mmnuradityo.openrepo.base.obtainVM
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import com.mmnuradityo.openrepo.databinding.FragmentRepositoryBinding
import com.mmnuradityo.openrepo.ui.activity.MainActivity
import com.mmnuradityo.openrepo.viewmodel.RepositoryVM

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class RepositoryFragment : BaseFragment<RepositoryFragment>() {

    private lateinit var viewBinding: FragmentRepositoryBinding
    private var dataRepository: GithubRepository? = null
    private var v: View? = null

    companion object {
        const val DATA_REPOSITORY = "data_repository"

        fun newInstance(data: GithubRepository): RepositoryFragment {
            val bundle = Bundle()
            bundle.putParcelable(DATA_REPOSITORY, data)
            return generateInstance(RepositoryFragment(), bundle)
        }
    }

    override fun initComponent(savedInstanceState: Bundle?) {
        dataRepository = arguments?.getParcelable(DATA_REPOSITORY)
    }

    override fun initViewComponent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRepositoryBinding.inflate(inflater, container, false)
        v = viewBinding.root
        return v
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        v?.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.zoom_in))
        (activity as MainActivity).showSearch(false)

        viewBinding.vm = getComponent().obtainVM(RepositoryVM::class.java).apply {
            dataRepository?.let {
                data.set(it)
                start()
            }
        }
    }

    override fun listener() {
    }
}