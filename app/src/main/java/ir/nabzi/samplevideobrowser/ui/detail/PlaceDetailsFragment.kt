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
//        initView()
    }

    private fun initView() {
        videoPlayer.useController = false
        //preparePlayerForContent()
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
            initView()
        initializePlayer()
        if (player?.playbackState != Player.STATE_READY)
            preparePlayerForContent()

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
        player?.seekTo(currentWindow, playbackPosition)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.orientationLandscape =
            getResources().getConfiguration().orientation === Configuration.ORIENTATION_LANDSCAPE
        initializePlayer()
        if (player?.playbackState != Player.STATE_READY)
            preparePlayerForContent()

    }
//
//        // Checking the orientation of the screen
//        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
////            val params = cvMedia.getLayoutParams() as ConstraintLayout.LayoutParams
////            params.width = ViewGroup.LayoutParams.MATCH_PARENT
////            params.height = ViewGroup.LayoutParams.MATCH_PARENT
////
////            cvMedia.setLayoutParams(params)
//
//        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
//            val params = cvMedia.getLayoutParams() as ConstraintLayout.LayoutParams
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT
//            params.height = 300
//            cvMedia.setLayoutParams(params)
//
//        }
//    }

}