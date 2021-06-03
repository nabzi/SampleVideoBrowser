package ir.nabzi.samplevideobrowser.ui.detail

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import ir.nabzi.samplevideobrowser.databinding.FragmentContentDetailsBinding
import ir.nabzi.samplevideobrowser.ui.home.ContentViewModel
import ir.nabzi.samplevideobrowser.ui.prepareVideo
import kotlinx.android.synthetic.main.fragment_content_details.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class ContentDetailsFragment : Fragment() {

    private val vmodel: ContentViewModel by sharedViewModel()
    var player: SimpleExoPlayer? = null
    lateinit var binding : FragmentContentDetailsBinding

    val STATE_RESUME_POSITION = "resume_position"
    val STATE_RESUME_WINDOW = "resume_window"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentDetailsBinding.inflate(inflater, container, false)
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

        btnPlay.visibility = View.GONE
        if (player?.playbackState != Player.STATE_READY)
            preparePlayerForContent()
        videoPlayer.useController = true;
        player?.play()
    }

    private fun stopPlaying() {
        player?.stop()
        btnPlay.visibility = View.VISIBLE
        videoPlayer.useController = false;
    }

    private fun preparePlayerForContent() {
        vmodel.selectedContent.observe(viewLifecycleOwner, {
            it?.videoUrl?.let { url ->
                player?.stop(true)
                player?.prepareVideo(url.toUri(), requireContext())
            }
        })
    }
    override fun onResume() {
        super.onResume()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player?.release()
            player = null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, currentWindow)
        outState.putLong(STATE_RESUME_POSITION, playbackPosition)
        super.onSaveInstanceState(outState)
    }
    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            player = SimpleExoPlayer.Builder(requireContext()).build()
            videoPlayer.player = player
        }
        if (player?.playbackState != Player.STATE_READY)
            preparePlayerForContent()
        player?.seekTo(currentWindow, playbackPosition)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.orientationLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        initializePlayer()
    }

}