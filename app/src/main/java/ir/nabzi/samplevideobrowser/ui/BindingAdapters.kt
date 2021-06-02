package ir.nabzi.samplevideobrowser.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("layout_height")
fun setLayoutHeight(view: View, orientationLandscape: Boolean) {
    val layoutParams = view.layoutParams
    layoutParams.height =
        if (orientationLandscape) ConstraintLayout.LayoutParams.MATCH_PARENT else 400
    view.layoutParams = layoutParams
}

