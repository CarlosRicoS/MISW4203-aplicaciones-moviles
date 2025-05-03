package co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.PerformerDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface VinylsApiService {
    @GET("albums")
    suspend fun getAlbums(): List<AlbumDTO>

    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): AlbumDTO

    @GET("musicians/{id}")
    suspend fun getMusicianById(@Path("id") id: Int): PerformerDTO

    @GET("bands/{id}")
    suspend fun getBandById(@Path("id") id: Int): PerformerDTO
}