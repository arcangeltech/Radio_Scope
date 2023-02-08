package marwen.project.radioscope.repositories

import marwen.project.radioscope.data.remote.RadioScopeApi
import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.data.remote.dto.RadioSearchDto
import marwen.project.radioscope.utils.Constants

import javax.inject.Inject

class RadioScopeRepositoryImpl @Inject constructor(private val api : RadioScopeApi) : RadioScopeRepository {
    override suspend fun getHomeRadioList(): RadioHomeDto {
        return api.getRadioHomeList(Constants.API_KEY,Constants.API_HOST,"")
    }

    override suspend fun getSearchRadioList(search:String,count:String?,page:String?): RadioSearchDto {
        return api.getRadioSearchList(Constants.API_KEY,Constants.API_HOST,search,count,page)
    }
}