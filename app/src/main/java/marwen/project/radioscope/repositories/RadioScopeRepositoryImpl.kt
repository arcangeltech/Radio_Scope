package marwen.project.radioscope.repositories

import marwen.project.radioscope.data.remote.RadioScopeApi
import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.utils.Constants

import javax.inject.Inject

class RadioScopeRepositoryImpl @Inject constructor(private val api : RadioScopeApi) : RadioScopeRepository {
    override suspend fun getHomeRadioList(): RadioHomeDto {
        return api.getRadioHomeList(Constants.API_KEY,Constants.API_HOST,"")
    }
}