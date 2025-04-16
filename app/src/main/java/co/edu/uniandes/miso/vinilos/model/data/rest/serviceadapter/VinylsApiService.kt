package co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.Album.AlbumDTO
import retrofit2.http.GET

interface VinylsApiService {
    @GET("albums")
    suspend fun getAlbums(): List<AlbumDTO>
}