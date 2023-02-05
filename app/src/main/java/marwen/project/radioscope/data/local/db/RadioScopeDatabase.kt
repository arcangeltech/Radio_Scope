package marwen.project.radioscope.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import marwen.project.radioscope.data.remote.dto.Radio

@Database(
    entities = [Radio::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RadioScopeDatabase: RoomDatabase() {
    abstract fun getRadioScopeDao (): RadioScopeDao

    companion object{
        @Volatile
        private var instance : RadioScopeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RadioScopeDatabase::class.java,
                "radio_scope_db.db"
            ).build()
    }
}