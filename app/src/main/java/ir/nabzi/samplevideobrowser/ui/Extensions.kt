package ir.nabzi.samplevideobrowser.ui

import android.app.AlertDialog
import android.content.*
import android.location.LocationManager
import android.net.Uri
import android.widget.Toast
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import ir.nabzi.samplevideobrowser.ui.util.CacheDataSourceFactory


fun Fragment.showError(message: String?) {
    Toast.makeText(requireContext(), message ?: "error", Toast.LENGTH_SHORT).show()
}

/***
 * ExoPlayer global media input
 */
fun SimpleExoPlayer.prepareVideo(uri: Uri, cacheContext: Context? = null) {
    val mediaItem: MediaItem = MediaItem.fromUri(uri)
    setMediaItem(mediaItem)
    playWhenReady = false
    // Read cache dir for prev versions
    cacheContext?.let {
        val medSource = ExtractorMediaSource(
            uri,
            CacheDataSourceFactory.getInstance(cacheContext, 100 * 1024 * 1024, 30 * 1024 * 1024),
            DefaultExtractorsFactory(), null, null
        )
        prepare(medSource, true, true)
        return
    }
    prepare()
}

