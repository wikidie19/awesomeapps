package com.awesome.apps.feature.home

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.awesome.apps.R
import com.awesome.apps.base.BaseFooterViewHolder
import com.awesome.apps.base.BaseListAdapter
import com.awesome.apps.base.BaseViewHolder
import com.awesome.apps.model.PhotosGallery
import kotlinx.android.synthetic.main.list_photo_landscape.view.*

class PhotoGalleryAdapter(context: Context) :
    BaseListAdapter<PhotosGallery, BaseViewHolder<*>>(context) {

    private var showFooter = false
    var isShowLoading = false
    var isLandscape = false

    init {
        withFooter = true
    }

    fun setLandscapeView(isLandscape: Boolean) {
        this.isLandscape = isLandscape
    }

    fun setShowFooter(showFooter: Boolean) {
        this.showFooter = showFooter
    }

    override fun getItemCount(): Int {
        return if (items.size > 0) items.size + 1 else 0
    }

    override fun getItemResourceLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            if (isLandscape) {
                return R.layout.list_photo_landscape
            } else {
                return R.layout.list_photo_portrait
            }
        } else {
            return R.layout.loading_footer
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) TYPE_FOOTER else TYPE_ITEM
    }

    fun isPositionFooter(position: Int): Boolean {
        return position == items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        var holding: BaseViewHolder<*>? = null
        if (viewType == TYPE_ITEM) {
            holding = ViewHolder(getView(parent, viewType))
        } else if (viewType == TYPE_FOOTER) {
            holding = FooterViewHolder(getView(parent, viewType))
        }

        return holding!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is ViewHolder) {
            super.onBindViewHolder(holder, position)
        } else if (holder is FooterViewHolder) {
            if (showFooter) holder.show() else holder.hide()
        }
    }

    inner class ViewHolder internal constructor(val view: View) :
        BaseViewHolder<PhotosGallery>(view, onItemClickListener) {

        override fun bind(item: PhotosGallery) {
            if (isLandscape) {
                val metrics = context.resources.displayMetrics
                val width = metrics.widthPixels / 3
                val height = width

                val layoutParams = itemView.ivPhotoThumbnail.layoutParams
                layoutParams.height = height
                layoutParams.width = height
                itemView.ivPhotoThumbnail.layoutParams = layoutParams

                Glide.with(context)
                    .load(item.src?.small)
                    .apply(
                        RequestOptions()
                            .override(height, width)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .skipMemoryCache(true)
                    )
                    .into(view.ivPhotoThumbnail)
            } else {
                val metrics = context.resources.displayMetrics
                val width = (metrics.widthPixels * .8f).toInt() - 32
                val height = (convertDpToPixel(250f, context) * .8f).toInt() - 16

                val layoutParams = itemView.ivPhotoThumbnail.layoutParams
                layoutParams.height = height
                itemView.ivPhotoThumbnail.layoutParams = layoutParams

                Glide.with(context)
                    .load(item.src?.small)
                    .apply(
                        RequestOptions()
                            .override(width, height)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .skipMemoryCache(true)
                    )
                    .into(view.ivPhotoThumbnail)
            }

            view.tvTitle.text = item.photographer
        }


        fun convertDpToPixel(dp: Float, context: Context): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            )
        }

    }

    inner class FooterViewHolder internal constructor(itemView: View) :
        BaseFooterViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.pg_load_more)

        override fun show() {
            progressBar.visibility = View.VISIBLE
        }

        override fun hide() {
            progressBar.visibility = View.GONE
        }

        override fun bind(item: Any?) {

        }

    }

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

}