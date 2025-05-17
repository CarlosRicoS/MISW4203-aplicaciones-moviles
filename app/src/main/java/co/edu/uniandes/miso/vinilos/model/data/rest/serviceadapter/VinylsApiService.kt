package co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CollectorAlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CollectorDTO
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

    @GET("musicians")
    suspend fun getMusicians(): List<PerformerDTO>

    @GET("bands")
    suspend fun getBands(): List<PerformerDTO>

    @GET("bands/{id}")
    suspend fun getBandById(@Path("id") id: Int): PerformerDTO

    @GET("collectors")
    suspend fun getCollectors(): List<CollectorDTO>

    @GET("collectors/{id}/performers")
    suspend fun getCollectorFavoritePerformers(@Path("id") id: Int): List<PerformerDTO>

    @GET("collectors/{id}/albums")
    suspend fun getCollectorAlbums(@Path("id") id: Int): List<CollectorAlbumDTO>
}