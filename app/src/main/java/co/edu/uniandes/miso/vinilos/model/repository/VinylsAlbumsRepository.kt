package co.edu.uniandes.miso.vinilos.model.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.PerformerDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.NetworkValidation
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.data.sqlite.VinylRoomDatabase
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Album
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Comment
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Track
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.mapper.AlbumMapper
import co.edu.uniandes.miso.vinilos.model.mapper.CommentMapper
import co.edu.uniandes.miso.vinilos.model.mapper.PerformerMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VinylsAlbumsRepository @Inject constructor(
    private val vinylsDatabase: VinylRoomDatabase,
    @ApplicationContext private val context: Context
){

    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getSimplifiedVinylsAlbums(): List<SimplifiedAlbum> {
        return withContext(Dispatchers.IO) {
            val albumDTOs = if (NetworkValidation.isNetworkAvailable(context))
                getVinylsAlbumsFromWebService() else getVinylsAlbumsFromLocalStorage()
            val albumsEntities = AlbumMapper.fromRestDtoListSimplifiedAlbums(albumDTOs)
            updateAlbumLocalStorage(albumDTOs)
            albumsEntities
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumDetailDTOById(id: Int): AlbumDTO {
        return withContext(Dispatchers.IO) {
            val albumDTO = if (NetworkValidation.isNetworkAvailable(context))
                vinylsApiService.getAlbumById(id) else getVinylsAlbumByIdFromLocalStorage(id)
            albumDTO
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsMusicianById(id: Int): PerformerDTO {
        return withContext(Dispatchers.IO) {
            val performerDTO = if (NetworkValidation.isNetworkAvailable(context))
                vinylsApiService.getMusicianById(id) else getPerformerById(id, PerformerType.MUSICIAN)
            updatePerformersTypeLocalStorage(listOf(performerDTO), PerformerType.MUSICIAN)
            performerDTO
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsBandById(id: Int): PerformerDTO {
        return withContext(Dispatchers.IO) {
            val performerDTO = if (NetworkValidation.isNetworkAvailable(context))
                vinylsApiService.getBandById(id) else getPerformerById(id, PerformerType.BAND)
            updatePerformersTypeLocalStorage(listOf(performerDTO), PerformerType.BAND)
            performerDTO
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumById(id: Int): DetailAlbum {
        return withContext(Dispatchers.IO) {
            val albumDTO = getVinylsAlbumDetailDTOById(id)
            AlbumMapper.fromRestDtoToDetailAlbum(albumDTO)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumPerformer(id: Int, performerType: PerformerType): SimplifiedPerformer? {
        return withContext(Dispatchers.IO) {
            if (performerType == PerformerType.MUSICIAN) {
                val performerDTO = getVinylsMusicianById(id)
                PerformerMapper.fromRestMusicianToSimplifiedPerformer(performerDTO)
            } else
            {
                val performer = getVinylsBandById(id)
                PerformerMapper.fromRestBandToSimplifiedPerformer(performer)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumPerformerByAlbumId(id: Int): SimplifiedPerformer? {
        return withContext(Dispatchers.IO) {
            val albumDTO = getVinylsAlbumDetailDTOById(id)

            val performers = albumDTO.performers
            if (performers.isNotEmpty()) {
                PerformerMapper.fromRestDtoToSimplifiedPerformer(performers[0])
            } else
            {
                null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumComments(id: Int): List<DetailComment> {
        return withContext(Dispatchers.IO) {
            val albumDTO = getVinylsAlbumDetailDTOById(id)
            val comments = albumDTO.comments
            CommentMapper.fromRestDtoListDetailComments(comments)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun updateAlbumLocalStorage(albums: List<AlbumDTO>) {
        if (!NetworkValidation.isNetworkAvailable(context)) return
        // @TODO add validation with data expiration
        val entitiesList = AlbumMapper.fromAlbumDTOToEntity(albums)
        vinylsDatabase.albumsDao().deleteAll()
        vinylsDatabase.albumsDao().insertAll(
            entitiesList[0] as List<Album>,
            entitiesList[1] as List<Performer>,
            entitiesList[2] as List<Comment>,
            entitiesList[3] as List<Track>
        )
    }

    private suspend fun getVinylsAlbumsFromWebService(): List<AlbumDTO> {
        return withContext(Dispatchers.IO) {
            vinylsApiService.getAlbums()
        }
    }

    private suspend fun getVinylsAlbumsFromLocalStorage(): List<AlbumDTO> {
        val albums = vinylsDatabase.albumsDao().getAllAlbums()

        return AlbumMapper.fromAlbumListEntityToDTO(
            albums[0] as List<Album>,
            albums[1] as List<Performer>,
            albums[2] as List<Comment>,
            albums[3] as List<Track>
        )
    }

    private suspend fun getVinylsAlbumByIdFromLocalStorage(id: Int): AlbumDTO {
        val albums = vinylsDatabase.albumsDao().getAlbumWithId(id)

        return AlbumMapper.fromAlbumEntityToDTO(
            albums[0] as List<Album>,
            albums[1] as List<Performer>,
            albums[2] as List<Comment>,
            albums[3] as List<Track>
        )
    }

    private suspend fun updatePerformersTypeLocalStorage(performers: List<PerformerDTO>, performerType: PerformerType) {
        performers.map {
                performer -> vinylsDatabase.performersDao()
            .updatePerformerType(performer.id, performerType.toString())
        }
    }

    private fun getPerformerById(performerId: Int, performerType: PerformerType): PerformerDTO {
        val performer = vinylsDatabase.performersDao().getPerformerByIdAndType(performerId, performerType.toString())

        return PerformerDTO(
            id = performer.id,
            name = performer.name,
            image = performer.image,
            description = performer.description,
            albums = listOf(),
        )
    }
}
