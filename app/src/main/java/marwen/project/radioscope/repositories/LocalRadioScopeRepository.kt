package marwen.project.radioscope.repositories

import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.utils.Resource

interface LocalRadioScopeRepository {
    suspend fun getFavorite(): Resource<List<Radio>>
    suspend fun insertFavorite(radio: Radio)
    suspend fun deleteFavorite(radio: Radio)
}