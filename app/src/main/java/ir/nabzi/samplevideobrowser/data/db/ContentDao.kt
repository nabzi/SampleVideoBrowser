package ir.nabzi.samplevideobrowser.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.nabzi.samplevideobrowser.model.Content
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(vararg content: Content)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addList(list: List<Content>)

    @Query("SELECT * FROM content " )
    abstract fun getContents(): List<Content>

    @Query("SELECT * FROM content WHERE title LIKE :search" )
    abstract fun getContentsFlow(search : String): Flow<List<Content>>

    @Query("Delete From content" )
    abstract suspend fun removeAll()
}