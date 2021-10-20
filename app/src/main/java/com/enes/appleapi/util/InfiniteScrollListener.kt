package com.enes.appleapi.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteScrollListener : RecyclerView.OnScrollListener {
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 4
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var currentPage = 0
    private var globalY = 0
    private var gridLayoutManager: GridLayoutManager? = null
        set(value) {
            field = value
        }
    private val loadMore = Runnable { onLoadMore(currentPage) }

    constructor(gridLayoutManager: GridLayoutManager?) {
        this.gridLayoutManager = gridLayoutManager
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = gridLayoutManager!!.itemCount
        firstVisibleItem = gridLayoutManager!!.findFirstVisibleItemPosition()
        globalY += dy
        scrollY(globalY)
        if (loading) {
            if (totalItemCount > previousTotal || totalItemCount == 0) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && globalY > 0 && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            recyclerView.post(loadMore)
            loading = true
        }
    }

    abstract fun scrollY(globalY: Int)
    abstract fun onLoadMore(currentPage: Int)


    fun clear() {
        gridLayoutManager!!.scrollToPosition(0)
        previousTotal = 0
        loading = true
        currentPage = 0
        globalY = 0
    }
}