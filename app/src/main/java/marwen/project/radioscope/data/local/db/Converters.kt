package marwen.project.radioscope.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import marwen.project.radioscope.data.remote.dto.Radio

class Converters {
    @TypeConverter
    fun restoreList(listOfString: String?): List<String?>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
    }

    @TypeConverter
    fun saveList(listOfString: List<String?>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun radioToString(radio: Radio): String {
        return Gson().toJson(radio)
    }

    @TypeConverter
    fun stringToRadio(radioString: String): Radio {
        val objectType = object : TypeToken<Radio>() {}.type
        return Gson().fromJson(radioString, objectType)
    }
}
