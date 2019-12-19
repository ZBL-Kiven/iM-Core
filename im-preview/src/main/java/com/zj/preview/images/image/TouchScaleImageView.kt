package com.zj.preview.images.image

import android.content.Context
import android.util.AttributeSet
import com.zj.preview.images.image.easing.Easing
import com.zj.preview.images.image.easing.ScaleEffect

internal class TouchScaleImageView : ImageViewTouch {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun getEasing(): Easing {
        return ScaleEffect.QUAD.easing
    }

}
