package com.mmnuradityo.openrepo.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class RvEndlessListener constructor(
    private val layoutManager: RecyclerView.LayoutManager?,
    val loadMore: (Int) -> Unit
) : RecyclerView.OnScrollListener() {

    private var scrollDown: () -> Unit = {}
    private var scrollUp: () -> Unit = {}
    private var visibleThreshold = 3
    private var currentPage: Int = 0
    private var previousTotal: Int = 0
    private var loading = true

    constructor(
        layoutManager: RecyclerView.LayoutManager?,
        threshold: Int,
        loadMore: (Int) -> Unit
    ) : this(layoutManager, loadMore) {
        this.visibleThreshold = threshold
    }

    constructor(
        layoutManager: RecyclerView.LayoutManager?,
        threshold: Int = 3,
        loadMore: (Int) -> Unit,
        scrollUp: () -> Unit = {}
    ) : this(layoutManager, loadMore) {
        this.visibleThreshold = threshold
        this.scrollUp = scrollUp
    }

    constructor(
        layoutManager: RecyclerView.LayoutManager?,
        threshold: Int = 3,
        loadMore: (Int) -> Unit,
        scrollUp: () -> Unit,
        scrollDown: () -> Unit
        ) : this(layoutManager, loadMore) {
        this.visibleThreshold = threshold
        this.scrollUp = scrollUp
        this.scrollDown = scrollDown
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            layoutManager?.let {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                var firstVisibleItemPosition = 0

                when (layoutManager) {
                    is LinearLayoutManager -> {
                        firstVisibleItemPosition =
                            layoutManager.findFirstCompletelyVisibleItemPosition()
                    }
                    is GridLayoutManager -> {
                        firstVisibleItemPosition =
                            layoutManager.findFirstCompletelyVisibleItemPosition()
                    }
                    is StaggeredGridLayoutManager -> {
                        var tmp: IntArray? = null
                        tmp = layoutManager.findFirstCompletelyVisibleItemPositions(tmp)

                        tmp?.let { if (tmp.isNotEmpty()) firstVisibleItemPosition = tmp[0] }
                    }
                }

                if (loading && totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
                    loading = true
                    currentPage++
                    loadMore(currentPage)
                }

                scrollDown
            }

        } else if (dy < 0) {
            scrollUp
        }
    }

}
