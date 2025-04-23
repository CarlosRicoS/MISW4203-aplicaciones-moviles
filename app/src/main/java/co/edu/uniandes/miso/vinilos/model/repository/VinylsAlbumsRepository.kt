package co.edu.uniandes.miso.vinilos.model.repository

import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import co.edu.uniandes.miso.vinilos.model.mapper.AlbumMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VinylsAlbumsRepository {
    
    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService
    
    suspend fun getSimplifiedVinylsAlbums(): List<SimplifiedAlbum> {
        return withContext(Dispatchers.IO) {
            val albumDTOs = vinylsApiService.getAlbums()
            AlbumMapper.fromRestDtoListSimplifiedAlbums(albumDTOs)
        }
    }

    suspend fun getDetailsVinylsAlbums(): List<DetailAlbum> {
        return withContext(Dispatchers.IO) {
            val albumDTOs = vinylsApiService.getAlbums()
            AlbumMapper.fromRestDtoListDetailAlbums(albumDTOs)
        }
    }
}
