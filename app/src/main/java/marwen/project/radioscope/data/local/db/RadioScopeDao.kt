package marwen.project.radioscope.data.local.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import marwen.project.radioscope.data.remote.dto.Radio

@Dao
interface RadioScopeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(radio: Radio)

    @Query("SELECT * FROM radio_table")
    suspend fun getFavorites(): List<Radio>

    @Delete
    suspend fun deleteFavorite(radio: Radio)

}