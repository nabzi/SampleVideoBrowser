package ir.nabzi.samplevideobrowser.ui

import android.app.AlertDialog
import android.content.*
import android.location.LocationManager
import android.widget.Toast
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment


fun Fragment.showError(message: String?) {
    Toast.makeText(requireContext(), message ?: "error", Toast.LENGTH_SHORT).show()
}
