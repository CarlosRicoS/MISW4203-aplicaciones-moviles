package co.edu.uniandes.miso.vinilos.model.repository

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.mapper.AlbumMapper
import co.edu.uniandes.miso.vinilos.model.mapper.CommentMapper
import co.edu.uniandes.miso.vinilos.model.mapper.PerformerMapper
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

    suspend fun getVinylsAlbumDetailDTOById(id: Int): AlbumDTO {
        return withContext(Dispatchers.IO) {
            val albumDTO = vinylsApiService.getAlbumById(id)
            albumDTO
        }
    }

    suspend fun getVinylsAlbumById(id: Int): DetailAlbum {
        return withContext(Dispatchers.IO) {
            val albumDTO = getVinylsAlbumDetailDTOById(id)
            AlbumMapper.fromRestDtoToDetailAlbum(albumDTO)
        }
    }

    suspend fun getVinylsAlbumPerformers(id: Int): List<SimplifiedPerformer> {
        return withContext(Dispatchers.IO) {
            val albumDTO = getVinylsAlbumDetailDTOById(id)
            val performers = albumDTO.performers
            PerformerMapper.fromRestDtoListSimplifiedPerformers(performers)
        }
    }

    suspend fun getVinylsAlbumComments(id: Int): List<DetailComment> {
        return withContext(Dispatchers.IO) {
            val albumDTO = getVinylsAlbumDetailDTOById(id)
            val comments = albumDTO.comments
            CommentMapper.fromRestDtoListDetailComments(comments)
        }
    }

}
