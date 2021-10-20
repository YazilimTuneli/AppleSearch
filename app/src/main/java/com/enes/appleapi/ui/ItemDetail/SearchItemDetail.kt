package com.enes.appleapi.ui.ItemDetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.enes.appleapi.databinding.BottomSheetItemDetailBinding
import com.enes.appleapi.model.Results
import com.enes.appleapi.util.getFormattedDate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.concurrent.TimeUnit
import android.content.Intent
import android.net.Uri


class SearchItemDetail: BottomSheetDialogFragment {
    var binding: BottomSheetItemDetailBinding? = null
    var results: Results? = null

    constructor():super(){}

    constructor(results: Results?):super(){
        this.results = results
    }

    companion object{
        fun newInstance(results: Results?): SearchItemDetail {
            return SearchItemDetail(results = results)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetItemDetailBinding.inflate(inflater,container,false)
        binding?.artworkUrl100?.let { binding?.root?.let { it1 -> Glide.with(it1).load(results?.artworkUrl100).into(it) } }
        binding?.artistName?.text = results?.artistName
        binding?.collectionName?.text = results?.collectionName
        binding?.collectionPrice?.text = results?.collectionPrice.toString()+"$"
        binding?.country?.text = results?.country
        binding?.currency?.text = results?.currency
        binding?.kind?.text = results?.kind
        binding?.time?.text = "${results?.trackTimeMillis?.let { TimeUnit.MILLISECONDS.toMinutes(it)}.toString()} minute"
        binding?.releaseDate?.text = results?.releaseDate?.getFormattedDate(false)
        binding?.trackPrice?.text = results?.trackPrice.toString() +"$"
        binding?.trackView?.text = if(results?.kind.equals(CATEGORIES.SONG.get)) "Listen to me!" else if(results?.kind.equals(CATEGORIES.SOFTWARE.get)) "Examine me!" else if(results?.kind.equals(CATEGORIES.BOOK.get)) "Read me!" else "Watch to me!"
        binding?.trackView?.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(results?.trackViewUrl))
            startActivity(browserIntent)
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight
            (binding?.itemDetailCoordinator?.parent as View).setBackgroundColor(Color.TRANSPARENT)
        }
    }


}

enum class CATEGORIES(val get: String){
    MOVIE("feature-movie"),
    SONG("song"),
    SOFTWARE("software"),
    BOOK("audiobook")
}