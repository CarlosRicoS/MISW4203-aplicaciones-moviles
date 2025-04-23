package co.edu.uniandes.miso.vinilos.model.repository

import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.domain.Album
import co.edu.uniandes.miso.vinilos.model.mapper.AlbumMapper

class VinylsAlbumsRepository {
    
    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService
    
    suspend fun getVinylsAlbums(): List<Album> {
        val albumDTOs = vinylsApiService.getAlbums()
        return AlbumMapper.fromDtoList(albumDTOs)
    }
}
