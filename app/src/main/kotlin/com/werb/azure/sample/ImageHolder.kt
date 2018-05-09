package com.werb.azure.sample

import android.view.View
import com.bumptech.glide.Glide
import com.werb.library.MoreViewHolder
import kotlinx.android.synthetic.main.layout_image.*

/**
 * Created by wanbo on 2018/5/9.
 */
class ImageHolder(containerView: View) : MoreViewHolder<String>(containerView) {

    override fun bindData(data: String, payloads: List<Any>) {
        Glide.with(containerView)
            .load(data)
            .into(image)
    }
}