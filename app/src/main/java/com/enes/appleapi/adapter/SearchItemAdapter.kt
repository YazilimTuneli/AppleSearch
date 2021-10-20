package com.enes.appleapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enes.appleapi.R
import com.enes.appleapi.ui.search.MainSearchDataModel
import com.enes.appleapi.ui.search.MainSearchDelegate
import com.enes.appleapi.util.getFormattedDate

class SearchItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var results : List<MainSearchDataModel>? = null
    var clickListener : MainSearchDelegate? = null
    constructor(results: List<MainSearchDataModel>?,clickListener:MainSearchDelegate ){
        this.results = results
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.search_item,parent,false)
        return SearchItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val searchItemHolder = holder as SearchItemViewHolder
        val item = results?.get(position)
        searchItemHolder.searchImageView?.let {
            Glide.with(searchItemHolder.itemView).load(item?.artworkUrl100).into(
                it
            )
        }
        searchItemHolder.searchCollectionName?.text = item?.collectionName
        searchItemHolder.searchCollectionPrice?.text = item?.collectionPrice?.toString() +"$"
        searchItemHolder.searchReleaseDate?.text = item?.date?.getFormattedDate()

        searchItemHolder.itemView.setOnClickListener {
            clickListener?.setClickListener(position)
        }
    }

    override fun getItemCount(): Int {
       return if (!results.isNullOrEmpty()) results?.size!! else 0
    }
    fun setList(results: List<MainSearchDataModel>?){
        this.results = results
    }
    fun clear(){
        (this.results as ArrayList<MainSearchDataModel>).clear()
    }
}

class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var searchCardView: CardView? = null
    var searchImageView: ImageView? = null
    var searchCollectionName: TextView? = null
    var searchRelative: RelativeLayout? = null
    var searchCollectionPrice: TextView? = null
    var searchReleaseDate: TextView? = null
    var view : View? = null

    init {
        searchCardView = view.findViewById(R.id.searchItemCardView)
        searchImageView = view.findViewById(R.id.searchItemImage)
        searchCollectionName = view.findViewById(R.id.searchItemaCollectionName)
        searchRelative = view.findViewById(R.id.searchRelative)
        searchCollectionPrice = view.findViewById(R.id.searchCollectionPrice)
        searchReleaseDate = view.findViewById(R.id.searchReleaseDate)
        this.view = view
    }

}
