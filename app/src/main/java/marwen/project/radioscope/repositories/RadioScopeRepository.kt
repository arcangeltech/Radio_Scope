package marwen.project.radioscope.repositories

import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.data.remote.dto.RadioSearchDto


interface RadioScopeRepository {
    suspend fun getHomeRadioList(): RadioHomeDto
    suspend fun getSearchRadioList(search:String,count:String?,page:String?): RadioSearchDto
}