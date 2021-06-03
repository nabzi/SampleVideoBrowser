package ir.nabzi.samplevideobrowser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ir.nabzi.samplevideobrowser.R
import ir.nabzi.samplevideobrowser.databinding.FragmentContentDetailsBinding
import ir.nabzi.samplevideobrowser.databinding.FragmentContentsBinding
import ir.nabzi.samplevideobrowser.model.Content
import ir.nabzi.samplevideobrowser.model.Status
import ir.nabzi.samplevideobrowser.ui.home.adapter.ContentAdapter
import ir.nabzi.samplevideobrowser.ui.showError
import kotlinx.android.synthetic.main.fragment_contents.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ContentsFragment : Fragment() {

    private val vmodel: ContentViewModel by sharedViewModel()
    private lateinit var adapter: ContentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContentsBinding.inflate(inflater, container, false)
        binding.vmodel = vmodel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        subscribeUi()
    }

    private fun subscribeUi() {
        vmodel.contentList?.observe(viewLifecycleOwner, Observer { resource ->
            resource?.data?.let {

                    showContents(it)
            }
            when (resource?.status) {
                Status.SUCCESS -> showProgress(false)
                Status.ERROR -> {
                    showProgress(false)
                    showError(resource.message)
                }
                Status.LOADING -> showProgress(true)
            }
        })
    }


    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }


    private fun showContents(Contents: List<Content>) {
        adapter.apply {
            list = Contents
            isMoreDataAvailable = true
            notifyDataChanged()
        }
    }

    private fun initAdapter() {
        adapter = object : ContentAdapter({ id -> goToContent(id) }) {
            override fun loadMore(lastItem: Content?) {}
        }
        rvContent.adapter = adapter
    }

    fun goToContent(id: String) {
        vmodel.selectedContentId.postValue(id)
        this.findNavController().navigate(
            ContentsFragmentDirections.actionContentsFragmentToContentDetailsFragment()
        )
    }

}
