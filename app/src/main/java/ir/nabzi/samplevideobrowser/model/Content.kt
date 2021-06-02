package ir.nabzi.samplevideobrowser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "content")
@Parcelize
data class Content
(
        @PrimaryKey
        val id: String,
        val title: String,
        val subTitle: String? = null,
        val imageUrl: String? = null,
        val rating: Int? = null,
        val location_lat: Double,
        val location_lng: Double

) : Parcelable
