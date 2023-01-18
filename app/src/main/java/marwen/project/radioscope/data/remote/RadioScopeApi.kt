package marwen.project.radioscope.data.remote

import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import retrofit2.http.*

interface RadioScopeApi {
    @Headers( "Content-Type: application/json;charset=UTF-8")
    @GET("api.php")
    suspend fun getRadioHomeList(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") apiHost: String,
        @Query("home") home:String?

    ): RadioHomeDto
}