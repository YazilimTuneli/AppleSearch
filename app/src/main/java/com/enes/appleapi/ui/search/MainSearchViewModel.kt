package com.enes.appleapi.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enes.appleapi.api.AppleService
import com.enes.appleapi.api.Constants
import com.enes.appleapi.api.Host
import com.enes.appleapi.model.Results
import com.enes.appleapi.model.Search
import com.enes.appleapi.util.JsonCast
import com.enes.appleapi.util.getSpaceReplace
import com.google.gson.JsonElement
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Flow
import kotlin.collections.ArrayList

class MainSearchFactory: ViewModelProvider.Factory{
    var delegate : MainSearchDelegate? = null
    constructor(mainSearchDelegate: MainSearchDelegate?){
        this.delegate = mainSearchDelegate
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return MainSearchViewModel(delegate) as T
    }

}

class MainSearchViewModel(private var delegate: MainSearchDelegate?): ViewModel() {
    var results: ArrayList<Results> = ArrayList()
    var error = MutableLiveData<Throwable>()
    var subscription :Subscription? = null


    fun getSearchData(service: AppleService, phrase: String?,entity: String? = "",page: Int = 1){
        unSubscribe()
        if (page == 1)
        delegate?.skeletonShow()
        val hashMap = HashMap<String, Any?>()
        hashMap["term"] = phrase?.getSpaceReplace()
        hashMap["limit"] = Constants.PAGE_LIMIT*page
        if (entity?.isNotEmpty() == true) hashMap["entity"] = entity

        val observer = object : rx.Observer<JsonElement?> {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                error.value = e
            }
            override fun onNext(jsonElement: JsonElement?) {
                results.clear()
                val search : Search? = jsonElement?.let { JsonCast.castClass(it,Search::class.java) }
                setData(search,page)
            }
        }
        subscription = service.getRequest(Host.BASE,hashMap)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(observer)

    }

    private fun setData(search: Search?,page: Int) {
        if (!search?.results.isNullOrEmpty()){
            val dataList: List<MainSearchDataModel> = ArrayList()
            results = search?.results as ArrayList<Results>
            for (r :Results in  results){
                r.collectionPrice?.let {
                    MainSearchDataModel(r.artworkUrl100,r.collectionName,
                        it,r.releaseDate)
                }?.let { (dataList as ArrayList<MainSearchDataModel>).add(it) }
            }
            if (page == 1) delegate?.setSearchList(dataList) else delegate?.setSearchPagination(dataList)
        }else{
            delegate?.emptyList()
        }

        delegate?.skeletonHide()
    }

    fun getResult(position: Int): Results?{
       return if (!results.isNullOrEmpty())  results.get(position) else null
    }
    fun unSubscribe(){
        if (subscription != null) subscription!!.unsubscribe()
    }
}

