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
        val videoUrl : String? = null,
        val videoLengthSec : Int = 0,
        val likes: Int? = null,
        val timesPlayed: Int? = null,
        val commentCount: Int? = null
        ) : Parcelable
