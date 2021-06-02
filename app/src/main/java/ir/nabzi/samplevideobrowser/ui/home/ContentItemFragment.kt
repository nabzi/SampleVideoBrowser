package ir.nabzi.samplevideobrowser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.nabzi.samplevideobrowser.model.Content

typealias CALLBACK = (String)->Unit

class ContentItemFragment : Fragment() {
    var clickListener :  CALLBACK? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ContentView = ContentView(layoutInflater, container, clickListener)
        requireArguments().getParcelable<Content>(KEY_Content)?.let{
            ContentView.bind(it)
        }
        return ContentView.binding.root
    }

    companion object {
        const val KEY_Content = "key_Content"
        fun create(Content: Content, listener: CALLBACK): ContentItemFragment {
            return ContentItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_Content, Content)
                }
                clickListener = listener
            }
        }
    }
}