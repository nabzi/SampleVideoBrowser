package ir.nabzi.samplevideobrowser.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ir.nabzi.samplevideobrowser.R
import ir.nabzi.samplevideobrowser.databinding.ItemContentLayoutBindingImpl
import ir.nabzi.samplevideobrowser.model.Content

class ContentView(layoutInflater: LayoutInflater, container: ViewGroup?, val onClick: CALLBACK?) {
    val binding : ItemContentLayoutBindingImpl = DataBindingUtil.inflate(
        layoutInflater,
        R.layout.item_content_layout, container,false
    )
    fun bind(Content: Content) {
        with(binding) {
            contentItem = Content
            cvContent.setOnClickListener {
                onClick?.invoke(Content.id)
            }
            executePendingBindings()
        }

    }
}

