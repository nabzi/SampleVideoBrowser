package ir.nabzi.samplevideobrowser.data.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.nabzi.samplevideobrowser.model.Content

/**
 * Main database description.
 */
@Database(
    entities = [
        Content::class],
    version = 1,
    exportSchema = false
)
abstract class DB : RoomDatabase() {

    abstract fun contentDao(): ContentDao

    companion object {
        private var instance: DB? = null

        @Synchronized
        fun get(context: Context): DB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java, "AppDatabase"
                )
                    .build()
            }
            return instance!!
        }
    }
}
