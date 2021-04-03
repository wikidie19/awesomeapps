package com.awesome.apps.feature.home

import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.awesome.apps.R
import com.awesome.apps.base.DataBindingActivity
import com.awesome.apps.base.EndlessRecyclerOnScrollListener
import com.awesome.apps.databinding.ActivityHomeBinding
import com.awesome.apps.feature.detail.DetailPhotoActivity
import com.awesome.apps.helper.NetworkHelper
import com.awesome.apps.network.model.Resource
import com.google.android.material.appbar.AppBarLayout
import org.koin.android.ext.android.inject

class HomeActivity : DataBindingActivity<ActivityHomeBinding>() {

    private val TAG = HomeActivity::class.java.simpleName

    private val vm by inject<HomeViewModel>()

    private var page = 1
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    lateinit var adapter: PhotoGalleryAdapter
    lateinit var progressLoading: ProgressDialog

    private var menuGrid: Drawable? = null
    private var menuList: Drawable? = null

    override fun layoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb.vm = vm
        vb.lifecycleOwner = this

        initAdapter()
        initLoadMore()
        onViewInit()
        onLoadData()
    }

    override fun onViewInit() {

        progressLoading = ProgressDialog(this)

        setSupportActionBar(vb.toolbar)

        var isShow = true
        var scrollRange = -1
        vb.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                vb.toolbarLayout.title = "Awesome Apps"
                vb.toolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.white))
                menuGrid?.setColorFilter(
                    getResources().getColor(R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
                menuList?.setColorFilter(
                    getResources().getColor(R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
                isShow = true
            } else if (isShow) {
                vb.toolbarLayout.title = " "
                menuGrid?.setColorFilter(
                    getResources().getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP
                )
                menuList?.setColorFilter(
                    getResources().getColor(R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP
                )
                isShow = false
            }
        })

        loadData(1)

        vb.btnReload.setOnClickListener {
            page = 1
            loadData(1)
        }
    }

    private fun initAdapter() {
        adapter = PhotoGalleryAdapter(this)

        gridLayoutManager = GridLayoutManager(this, 1)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.isPositionFooter(position)) gridLayoutManager.spanCount else 1
            }
        }
        vb.rvPhotosGallery.layoutManager = gridLayoutManager
        vb.rvPhotosGallery.setHasFixedSize(true)
        vb.rvPhotosGallery.adapter = adapter
        adapter.setLandscapeView(true)
        adapter.isShowLoading = true
        adapter.setShowFooter(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        menuGrid = menu?.getItem(0)?.icon
        menuList = menu?.getItem(1)?.icon
        menuGrid?.mutate()
        menuList?.mutate()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuGrid -> {
                vb.rvPhotosGallery.removeOnScrollListener(endlessRecyclerOnScrollListener)
                gridLayoutManager = GridLayoutManager(this, 2)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter.isPositionFooter(position)) gridLayoutManager.spanCount else 1
                    }
                }

                vb.rvPhotosGallery.layoutManager = gridLayoutManager
                adapter.setLandscapeView(false)

                vb.rvPhotosGallery.adapter = adapter

                initLoadMore()
                true
            }
            R.id.menuList -> {
                vb.rvPhotosGallery.removeOnScrollListener(endlessRecyclerOnScrollListener)
                gridLayoutManager = GridLayoutManager(this, 1)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter.isPositionFooter(position)) gridLayoutManager.spanCount else 1
                    }
                }

                vb.rvPhotosGallery.layoutManager = gridLayoutManager
                adapter.setLandscapeView(true)

                vb.rvPhotosGallery.adapter = adapter

                initLoadMore()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initLoadMore() {
        endlessRecyclerOnScrollListener =
            object : EndlessRecyclerOnScrollListener(gridLayoutManager, page, 16) {
                override fun onLoadMore(next: Int) {
                    page += 1
                    adapter.isShowLoading = false
                    loadData(page)
                }
            }

        vb.rvPhotosGallery.addOnScrollListener(endlessRecyclerOnScrollListener)
    }

    private fun loadData(page: Int) {
        if (!NetworkHelper.isConnected(this)) {
            vb.llErrorConnection.visibility = View.VISIBLE
        } else {
            vb.llErrorConnection.visibility = View.GONE
            showLoading()
            vm.getDataPhotoGallery(page, 16, "nature")
        }
    }

    override fun onLoadData() {
        vm.dataPhoto.observe(this, {
            when (vm.loadingPhoto.value) {
                Resource.Status.LOADING -> {

                }

                Resource.Status.SUCCESS -> {
                    vb.ivHeader.setImageDrawable(resources.getDrawable(R.drawable.ic_header))
                    progressLoading.dismiss()
                    adapter.setShowFooter(false)
                    adapter.addAll(it.photos)
                    adapter.notifyDataSetChanged()
                    adapter.setOnItemClickListener { _, position ->
                        DetailPhotoActivity.start(this, adapter.getItem(position))
                    }
                }

                Resource.Status.ERROR -> {
                    progressLoading.dismiss()
                    adapter.setShowFooter(false)
                }
                else -> {
                }
            }
        })
    }

    private fun showLoading() {
        if (adapter.isShowLoading) {
            progressLoading.setMessage("Please wait")
            progressLoading.show()
        } else {
            adapter.setShowFooter(true)
        }
    }

}