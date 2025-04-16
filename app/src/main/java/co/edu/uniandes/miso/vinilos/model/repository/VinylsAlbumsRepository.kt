package co.edu.uniandes.miso.vinilos.model.repository

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.Album.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter


interface VinylsAlbumsRepository {
    suspend fun getVinylsAlbums(): List<AlbumDTO>
}

class NetworkVinylsAlbumsRepository(
    private val vinylsApiService: VinylsApiService
) : VinylsAlbumsRepository {

    override suspend fun getVinylsAlbums(): List<AlbumDTO> = vinylsApiService.getAlbums()
}
