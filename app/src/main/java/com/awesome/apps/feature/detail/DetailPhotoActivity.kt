package com.awesome.apps.feature.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.awesome.apps.R
import com.awesome.apps.base.DataBindingActivity
import com.awesome.apps.databinding.ActivityDetailPhotoBinding
import com.awesome.apps.model.PhotosGallery
import com.google.android.material.appbar.AppBarLayout
import org.koin.android.ext.android.inject


class DetailPhotoActivity : DataBindingActivity<ActivityDetailPhotoBinding>() {

    private val vm by inject<DetailPhotoViewModel>()

    companion object {

        private var photosGallery: PhotosGallery? = null

        fun start(context: Context, photosGallery: PhotosGallery) {
            val intent = Intent(context, DetailPhotoActivity::class.java)
            this.photosGallery = photosGallery
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_detail_photo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb.vm = vm
        vb.lifecycleOwner = this

        onViewInit()
        onLoadData()
    }

    override fun onViewInit() {
        val upArrow = resources.getDrawable(R.drawable.ic_arrow_back)
        setSupportActionBar(vb.toolbar)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var isShow = true
        var scrollRange = -1
        vb.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                vb.toolbarLayout.title = photosGallery?.photographer
                vb.toolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.white))
                isShow = true
            } else if (isShow) {
                vb.toolbarLayout.title = " "
                isShow = false
            }
        })

    }

    override fun onLoadData() {
        Glide.with(this)
            .load(photosGallery?.src?.portrait)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(true)
            )
            .into(vb.ivHeader)

        loadUrl(photosGallery?.photographerUrl)
    }


    private fun loadUrl(url: String?) {
        vb.wvDetailPhoto.loadUrl("$url")
        vb.wvDetailPhoto.settings.builtInZoomControls = true
        vb.wvDetailPhoto.settings.displayZoomControls = false
        vb.wvDetailPhoto.settings.setSupportZoom(false)
        vb.wvDetailPhoto.settings.javaScriptEnabled = true
        vb.wvDetailPhoto.settings.domStorageEnabled = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}