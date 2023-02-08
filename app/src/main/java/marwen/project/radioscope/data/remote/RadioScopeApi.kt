package marwen.project.radioscope.data.remote

import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.data.remote.dto.RadioSearchDto
import retrofit2.http.*

interface RadioScopeApi {
    @Headers( "Content-Type: application/json;charset=UTF-8")
    @GET("api.php")
    suspend fun getRadioHomeList(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") apiHost: String,
        @Query("home") home:String?

    ): RadioHomeDto

    @Headers( "Content-Type: application/json;charset=UTF-8")
    @GET("api.php")
    suspend fun getRadioSearchList(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") apiHost: String,
        @Query("search") search:String,
        @Query("search") count:String?,
        @Query("search") page:String?

    ): RadioSearchDto
}