package marwen.project.radioscope.repositories

import marwen.project.radioscope.data.remote.dto.RadioHomeDto


interface RadioScopeRepository {
    suspend fun getHomeRadioList(): RadioHomeDto
}