package com.enes.appleapi.ui.search

interface MainSearchDelegate {
    fun setSearchList(result: List<MainSearchDataModel>?)
    fun setSearchPagination(result: List<MainSearchDataModel>?)
    fun setClickListener(position:Int)
    fun skeletonShow()
    fun skeletonHide()
    fun emptyList()
}