package com.enes.appleapi.ui.search

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enes.appleapi.R
import com.enes.appleapi.adapter.SearchItemAdapter
import com.enes.appleapi.api.AppleService
import com.enes.appleapi.api.Constants
import com.enes.appleapi.api.RetrofitManager
import com.enes.appleapi.databinding.ActivityMainSearchBinding
import com.enes.appleapi.ui.ItemDetail.SearchItemDetail
import com.enes.appleapi.util.InfiniteScrollListener
import com.enes.appleapi.util.Util
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import java.lang.Exception
import lib.kingja.switchbutton.SwitchMultiButton
import lib.kingja.switchbutton.SwitchMultiButton.OnSwitchListener


class MainSearchActivity : AppCompatActivity(), MainSearchDelegate {
    var binding: ActivityMainSearchBinding? = null
    var searchViewModel: MainSearchViewModel? = null
    var searchFactory: MainSearchFactory? = null
    var service: AppleService? = null
    var searchItemAdapter: SearchItemAdapter? = null
    var skeletonScreen: SkeletonScreen? = null
    var page: Int = 1
    var infiniteScroolListener: InfiniteScrollListener? = null
    var layoutManager: GridLayoutManager? = null
    var searchItemDetail: SearchItemDetail? = null
    var entity: String? = ""

    override fun onStart() {
        super.onStart()
        binding?.searchEdt?.requestFocus()
        binding?.searchEdt?.isEnabled = true
        binding?.root?.let { Util.showKeyboard(this, it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_search)
        service = AppleService(RetrofitManager.getRetrofit(this))
        searchFactory = MainSearchFactory(this)
        searchViewModel = ViewModelProvider(this, searchFactory!!).get(MainSearchViewModel::class.java)

        searchItemAdapter = SearchItemAdapter(ArrayList(), this)
        layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        binding?.searchRecyclerView?.layoutManager = layoutManager
        binding?.searchRecyclerView?.setHasFixedSize(true)


        val tabTexts : Array<String> = resources.getStringArray(R.array.switch_tab)
        entity = tabTexts[0]
        binding?.searchSwitch?.setText(*tabTexts)?.setOnSwitchListener(onSwitchListener)

        setSkeletonScreen()
        viewVisibilityControl(false)
        addTextChangedListener()

        infiniteScroolListener = object : InfiniteScrollListener(layoutManager) {
            override fun scrollY(globalY: Int) {
                if (globalY> 90){
                    binding?.root?.let { Util.hideKeyboard(this@MainSearchActivity, it) }
                }
            }

            override fun onLoadMore(currentPage: Int) {
                searchViewModel?.getSearchData(service!!,getSearchText(),entity = entity,page = ++page)
            }

        }
        binding?.searchRecyclerView?.addOnScrollListener(infiniteScroolListener as InfiniteScrollListener)
        binding?.searchEdt?.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!getSearchText().isNullOrEmpty() && getSearchText()?.length!! > 1) {
                    binding?.root?.let { Util.hideKeyboard(this, it) }
                }
            }
            true
        }
        binding?.deleteIcon?.setOnClickListener {
            binding?.searchEdt?.setText("")
        }

        searchViewModel?.error?.observe(
            this,
            { error: Throwable? ->
                error?.let {
                    Toast.makeText(
                        this,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    val onSwitchListener =
        OnSwitchListener { position, tabText ->
            if (!getSearchText().isNullOrEmpty()) {
                service?.let {
                    searchViewModel?.getSearchData(it, getSearchText(),tabText)
                }
                entity = tabText
            }
        }



    private fun setSkeletonScreen() {
        skeletonScreen = Skeleton.bind(binding?.searchRecyclerView).adapter(searchItemAdapter)
            .shimmer(true)
            .angle(Constants.SKELETON_ANGLE)
            .frozen(false)
            .duration(Constants.SKELETON_DURATION)
            .count(Constants.SKELETON_COUNT)
            .load(R.layout.skeleton_search)
            .show()
        skeletonHide()
    }

    private fun infiniteScroolClear() {
        page = 1
        infiniteScroolListener?.clear()
    }

    fun getSearchText(): String? {
        return binding?.searchEdt?.text?.toString()?.trim { it <= ' ' }
    }

    override fun onStop() {
        super.onStop()
        binding?.root?.let { Util.hideKeyboard(this, it) }
    }

    override fun setSearchList(result: List<MainSearchDataModel>?) {
        searchItemAdapter?.clear()
        searchItemAdapter?.setList(results = result)
        searchItemAdapter?.notifyDataSetChanged()
    }

    override fun setSearchPagination(result: List<MainSearchDataModel>?) {
        searchItemAdapter?.setList(results = result)
        searchItemAdapter?.notifyItemRangeInserted(Constants.PAGE_LIMIT*(page-1),result?.size!! )
    }


    override fun setClickListener(position: Int) {
        Util.hideKeyboard(this, binding?.root!!)
        searchViewModel?.getResult(position)?.let {
            if (searchItemDetail != null && searchItemDetail?.isVisible == true)
                searchItemDetail?.dismiss()
            else{
                searchItemDetail = SearchItemDetail.newInstance(it)
                searchItemDetail?.show(this.supportFragmentManager,"ItemDetail")
            }
        }
    }

    override fun skeletonShow() {
        skeletonScreen?.show()
    }

    override fun skeletonHide() {
        skeletonScreen?.hide()
    }

    override fun emptyList() {
        binding?.searchTextView?.text = getString(R.string.couldnt_find)
        viewVisibilityControl(false)
    }

    fun goneSearchResult() {
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                binding?.searchTextView?.setText(R.string.can_search)
                viewVisibilityControl(false)
                searchItemAdapter?.clear()
                searchItemAdapter?.notifyDataSetChanged()

            } catch (e: Exception) {
            }
        }, 50)
    }

    fun viewVisibilityControl(visibility: Boolean) {
        if (!visibility) {
            binding?.searchFrameLayout?.visibility = View.VISIBLE
            binding?.searchRecyclerView?.visibility = View.GONE
        } else {
            binding?.searchFrameLayout?.visibility = View.GONE
            binding?.searchRecyclerView?.visibility = View.VISIBLE
        }
    }


    private fun addTextChangedListener() {
        binding?.searchEdt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!getSearchText().isNullOrEmpty() && binding?.searchEdt?.hasFocus()!! && getSearchText()?.length!! > 1) {
                    searchViewModel?.getSearchData(service!!, getSearchText(),entity)
                    viewVisibilityControl(true)
                } else {
                    goneSearchResult()
                }
                if (!getSearchText().isNullOrEmpty()) binding?.deleteIcon?.visibility =
                    View.VISIBLE else binding?.deleteIcon?.visibility = View.GONE
                infiniteScroolClear()

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        if (newOverride.fontScale > 1.30f) {
            newOverride.fontScale = 1f
            applyOverrideConfiguration(newOverride)
        }
        super.attachBaseContext(newBase)

    }

}