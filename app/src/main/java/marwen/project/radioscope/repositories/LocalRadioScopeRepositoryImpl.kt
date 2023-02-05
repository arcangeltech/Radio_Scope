package marwen.project.radioscope.repositories

import marwen.project.radioscope.data.local.db.RadioScopeDao
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.utils.Resource
import javax.inject.Inject

class LocalRadioScopeRepositoryImpl  @Inject constructor(private val dao : RadioScopeDao )  : LocalRadioScopeRepository{
    override suspend fun getFavorite(): Resource<List<Radio>> {
        return try {
            Resource.Success(dao.getFavorites())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "an error occured")
        }
    }

    override suspend fun insertFavorite(radio: Radio) {
         dao.insertFavorite(radio)
    }

    override suspend fun deleteFavorite(radio: Radio) {
        dao.deleteFavorite(radio)
    }
}