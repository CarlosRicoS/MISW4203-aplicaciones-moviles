package co.edu.uniandes.miso.vinilos.model.repository

import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.domain.Album
import co.edu.uniandes.miso.vinilos.model.mapper.AlbumMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VinylsAlbumsRepository {
    
    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService
    
    suspend fun getVinylsAlbums(): List<Album> {
        return withContext(Dispatchers.IO) {
            val albumDTOs = vinylsApiService.getAlbums()
            AlbumMapper.fromDtoList(albumDTOs)
        }
    }
}
