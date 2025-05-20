package co.edu.uniandes.miso.vinilos.model.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CommentCollector
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.AddCommentRequest
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CreateAlbumRequest
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.PerformerDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.NetworkValidation
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.data.sqlite.VinylRoomDatabase
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Album
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Comment
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Track
import co.edu.uniandes.miso.vinilos.model.domain.NewAlbum
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.mapper.AlbumMapper
import co.edu.uniandes.miso.vinilos.model.mapper.CommentMapper
import co.edu.uniandes.miso.vinilos.model.mapper.PerformerMapper
import co.edu.uniandes.miso.vinilos.model.settings.VinylsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

private const val DELAY_IN_MILLIS = 1000 * 60 * 10
private const val EXPIRATION_ALBUM_DATA_VALUE = "EXPIRATION_ALBUM_DATA_VALUE"

class VinylsAlbumsRepository @Inject constructor(
    private val vinylsDatabase: VinylRoomDatabase,
    @ApplicationContext private val context: Context
) {

    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getSimplifiedAlbums(): List<SimplifiedAlbum> {
        return withContext(Dispatchers.IO) {

            val dateInMillis = Date().time
            val expirationDate =
                VinylsDataStore.readLongProperty(context, EXPIRATION_ALBUM_DATA_VALUE)
            val albumsToReturn: List<SimplifiedAlbum>
            if (dateInMillis < expirationDate) {

                albumsToReturn = getSimplifiedAlbumsFromLocalStorage()
            } else {

                if (NetworkValidation.isNetworkAvailable(context)) {

                    val albumDTOs = getAlbumsFromWebService()
                    updateAlbumLocalStorage(albumDTOs)
                    VinylsDataStore.writeLongProperty(
                        context,
                        EXPIRATION_ALBUM_DATA_VALUE,
                        dateInMillis + (DELAY_IN_MILLIS)
                    )
                    albumsToReturn = AlbumMapper.fromRestDtoListSimplifiedAlbums(albumDTOs)
                } else {
                    albumsToReturn = getSimplifiedAlbumsFromLocalStorage()
                }
            }
            albumsToReturn
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getDetailedAlbumById(id: Int): DetailAlbum {
        return withContext(Dispatchers.IO) {
            val albumDTO = getAlbumDTOById(id)
            AlbumMapper.fromRestDtoToDetailAlbum(albumDTO)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumPerformer(
        id: Int,
        performerType: PerformerType
    ): SimplifiedPerformer? {
        return withContext(Dispatchers.IO) {
            if (performerType == PerformerType.MUSICIAN) {
                val performerDTO = getVinylsMusicianById(id)
                PerformerMapper.fromRestMusicianToSimplifiedPerformer(performerDTO)
            } else {
                val performer = getVinylsBandById(id)
                PerformerMapper.fromRestBandToSimplifiedPerformer(performer)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumPerformerByAlbumId(id: Int): SimplifiedPerformer? {
        return withContext(Dispatchers.IO) {
            val albumDTO = getAlbumDTOById(id)

            val performers = albumDTO.performers
            if (performers.isNotEmpty()) {
                PerformerMapper.fromRestDtoToSimplifiedPerformer(performers[0])
            } else {
                null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getVinylsAlbumComments(id: Int): List<DetailComment> {
        return withContext(Dispatchers.IO) {
            val albumDTO = getAlbumDTOById(id)
            val comments = albumDTO.comments
            CommentMapper.fromRestDtoListDetailComments(comments)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getAlbumDTOById(id: Int): AlbumDTO {
        return withContext(Dispatchers.IO) {
            val dateInMillis = Date().time
            val expirationDate =
                VinylsDataStore.readLongProperty(context, EXPIRATION_ALBUM_DATA_VALUE)
            val album: AlbumDTO

            if (dateInMillis < expirationDate) {

                album = getDetailedAlbumByIdFromLocalStorage(id)
            } else {

                if (NetworkValidation.isNetworkAvailable(context)) {

                    album = vinylsApiService.getAlbumById(id)
                } else {
                    album = getDetailedAlbumByIdFromLocalStorage(id)
                }
            }
            album
        }
    }

    suspend fun createAlbum(newAlbum: NewAlbum) {

        withContext(Dispatchers.IO) {

            val createAlbumRequest = CreateAlbumRequest(
                name = newAlbum.name,
                description = newAlbum.description,
                recordLabel = newAlbum.recordLabel,
                genre = newAlbum.genre,
                releaseDate = newAlbum.releaseDate,
                cover = newAlbum.cover
            )
            val createdAlbum = vinylsApiService.createAlbum(createAlbumRequest)

            if (newAlbum.performerType == PerformerType.MUSICIAN) {

                vinylsApiService.addMusicianToAlbum(createdAlbum.id, newAlbum.performerId)
            } else {
                vinylsApiService.addBandToAlbum(createdAlbum.id, newAlbum.performerId)
            }
            VinylsDataStore.writeLongProperty(
                context,
                EXPIRATION_ALBUM_DATA_VALUE,
                -1
            )
        }
    }

    suspend fun addComment(idAlbum: Int, content:String, rating: Int, idCollector:Int) {

        withContext(Dispatchers.IO) {

            val addCommentRequest = AddCommentRequest(content, rating, CommentCollector(idCollector))
            val response = vinylsApiService.addCommentToAlbum(idAlbum, addCommentRequest)
            val newComment = Comment(response.id, response.description, response.rating, idAlbum)
            vinylsDatabase.albumsDao().insertComments(listOf(newComment))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getVinylsMusicianById(id: Int): PerformerDTO {
        return withContext(Dispatchers.IO) {
            val performerDTO = if (NetworkValidation.isNetworkAvailable(context))
                vinylsApiService.getMusicianById(id) else getPerformerById(
                id,
                PerformerType.MUSICIAN
            )
            updatePerformersTypeLocalStorage(listOf(performerDTO), PerformerType.MUSICIAN)
            performerDTO
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getVinylsBandById(id: Int): PerformerDTO {
        return withContext(Dispatchers.IO) {
            val performerDTO = if (NetworkValidation.isNetworkAvailable(context))
                vinylsApiService.getBandById(id) else getPerformerById(id, PerformerType.BAND)
            updatePerformersTypeLocalStorage(listOf(performerDTO), PerformerType.BAND)
            performerDTO
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun updateAlbumLocalStorage(albums: List<AlbumDTO>) {

        val entitiesList = AlbumMapper.fromAlbumDTOToEntity(albums)
        vinylsDatabase.albumsDao().deleteAll()
        vinylsDatabase.albumsDao().insertAll(
            entitiesList[0] as List<Album>,
            entitiesList[1] as List<Performer>,
            entitiesList[2] as List<Comment>,
            entitiesList[3] as List<Track>
        )
    }

    private suspend fun getAlbumsFromWebService(): List<AlbumDTO> {

        return withContext(Dispatchers.IO) {
            val albums = vinylsApiService.getAlbums()
            albums
        }
    }

    private suspend fun getSimplifiedAlbumsFromLocalStorage(): List<SimplifiedAlbum> {

        val albums = vinylsDatabase.albumsDao().getAllAlbums()
        return AlbumMapper.fromAlbumListEntityToDTO(albums)
    }

    private suspend fun getDetailedAlbumByIdFromLocalStorage(id: Int): AlbumDTO {
        val albums = vinylsDatabase.albumsDao().getAlbumWithId(id)

        return AlbumMapper.fromAlbumEntityToDTO(
            albums[0] as List<Album>,
            albums[1] as List<Performer>,
            albums[2] as List<Comment>,
            albums[3] as List<Track>
        )
    }

    private suspend fun updatePerformersTypeLocalStorage(
        performers: List<PerformerDTO>,
        performerType: PerformerType
    ) {
        performers.map { performer ->
            vinylsDatabase.performersDao()
                .updatePerformerType(performer.id, performerType.toString())
        }
    }

    private fun getPerformerById(performerId: Int, performerType: PerformerType): PerformerDTO {
        val performer = vinylsDatabase.performersDao()
            .getPerformerByIdAndType(performerId, performerType.toString())

        return PerformerDTO(
            id = performer.id,
            name = performer.name,
            image = performer.image,
            description = performer.description,
            albums = listOf(),
        )
    }
}
