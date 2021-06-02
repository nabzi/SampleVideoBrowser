package ir.nabzi.samplevideobrowser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.nabzi.samplevideobrowser.R
import ir.nabzi.samplevideobrowser.databinding.FragmentContentDetailsBinding
import ir.nabzi.samplevideobrowser.ui.home.ContentViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ContentDetailsFragment : Fragment() {

    private val vmodel: ContentViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContentDetailsBinding.inflate(inflater, container, false)
        binding.vmodel = vmodel
        binding.lifecycleOwner = this
        return binding.root
    }

}