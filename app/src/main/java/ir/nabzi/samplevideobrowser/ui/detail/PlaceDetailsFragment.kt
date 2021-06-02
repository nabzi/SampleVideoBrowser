package ir.nabzi.samplevideobrowser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import ir.nabzi.samplevideobrowser.databinding.FragmentContentDetailsBinding
import ir.nabzi.samplevideobrowser.ui.home.ContentViewModel
import ir.nabzi.samplevideobrowser.ui.prepareVideo
import kotlinx.android.synthetic.main.fragment_content_details.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class ContentDetailsFragment : Fragment() {

    private val vmodel: ContentViewModel by sharedViewModel()
    var player: SimpleExoPlayer? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentContentDetailsBinding.inflate(inflater, container, false)
        binding.vmodel = vmodel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        videoPlayer.useController = false
        preparePlayerForContent()
        btnPlay.visibility = View.VISIBLE


        btnPlay.setOnClickListener {
            player?.run {
                if (isPlaying)
                    stopPlaying()
                else
                    startPlaying()
            }
        }
    }
    private fun startPlaying() {
        player?.play()
        btnPlay.visibility = View.GONE
        if (player?.playbackState != Player.STATE_READY)
            preparePlayerForContent()
        videoPlayer.useController = true;
    }

    private fun stopPlaying() {
        player?.stop()
        btnPlay.visibility = View.VISIBLE
        videoPlayer.useController = false;
    }

    private fun preparePlayerForContent() {
        vmodel.selectedContent.observe(viewLifecycleOwner , {
            it?.videoUrl?.let { url ->
                player?.stop(true)
                player?.prepareVideo(url.toUri(), requireContext())
            }
        })
    }
}