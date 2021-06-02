package ir.nabzi.samplevideobrowser.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import ir.nabzi.samplevideobrowser.R
import ir.nabzi.samplevideobrowser.databinding.ItemContentLayoutBinding
import ir.nabzi.samplevideobrowser.model.Content

abstract class ContentAdapter( val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<Content>? = null
    var isLoading = MutableLiveData(false)
    var isMoreDataAvailable = true //todo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder =
            ContentViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.item_content_layout, parent, false
                    )
            )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item = list?.get(position)
        if (item != null) {
            (holder as ContentViewHolder).bindTo(item);
        }
        if ( position >= itemCount - 1 && isMoreDataAvailable && (isLoading.value == true).not()) {
            isLoading.value = true;
            loadMore(item);
        }
    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
    abstract fun loadMore(lastItem : Content?)

    fun notifyDataChanged() {
        notifyDataSetChanged()
        isLoading.value = false
    }

    inner class ContentViewHolder(
            val binding: ItemContentLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var aContent: Content? = null
        fun bindTo(item: Content) {
            this.aContent = item
            with(binding) {
                contentItem = item
                binding.cvContent.setOnClickListener {
                    aContent?.let {
                        onItemClick(it.id)
                    }
                }
                executePendingBindings()
            }
        }
    }
}
