package co.edu.uniandes.miso.vinilos.model.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CollectorDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.NetworkValidation
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.data.sqlite.VinylRoomDatabase
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.CollectorAlbum
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.CollectorFavoritePerformer
import co.edu.uniandes.miso.vinilos.model.domain.DetailCollector
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector
import co.edu.uniandes.miso.vinilos.model.mapper.CollectorMapper
import co.edu.uniandes.miso.vinilos.model.settings.VinylsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

private const val DELAY_IN_MILLIS = 1000 * 60 * 10
private const val EXPIRATION_COLLECTOR_DATA_VALUE = "EXPIRATION_COLLECTOR_DATA_VALUE"
private const val EXPIRATION_COLLECTOR_PERFORMERS_VALUE = "EXPIRATION_COLLECTOR_PERFORMERS_VALUE"
private const val EXPIRATION_COLLECTOR_ALBUMS_VALUE = "EXPIRATION_COLLECTOR_ALBUMS_VALUE"

class VinylsCollectorsRepository @Inject constructor(
    private val vinylsDatabase: VinylRoomDatabase,
    @ApplicationContext private val context: Context
) {

    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getSimplifiedCollectors(): List<SimplifiedCollector> {
        return withContext(Dispatchers.IO) {

            val dateInMillis = Date().time
            val expirationDate = VinylsDataStore.readLongProperty(context, EXPIRATION_COLLECTOR_DATA_VALUE)
            val collectors: List<CollectorDTO>
            if (dateInMillis < expirationDate) {

                collectors = getCollectorsFromLocalStorage()
            } else {

                if(NetworkValidation.isNetworkAvailable(context)) {

                    collectors = vinylsApiService.getCollectors()
                    updateCollectorLocalStorage(collectors)
                    VinylsDataStore.writeLongProperty(context, EXPIRATION_COLLECTOR_DATA_VALUE, dateInMillis + (DELAY_IN_MILLIS))
                }
                else
                {
                    collectors = getCollectorsFromLocalStorage()
                }
            }

            CollectorMapper.fromRestDtoListSimplifiedCollectors(collectors)
        }
    }

    private fun getCollectorsFromLocalStorage(): List<CollectorDTO> {
        val collectors = vinylsDatabase.collectorsDao().getCollectors()
        return CollectorMapper.fromCollectorListEntityToDTO(collectors)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun updateCollectorLocalStorage(collectors: List<CollectorDTO>) {
        vinylsDatabase.collectorsDao().deleteAll()
        vinylsDatabase.collectorsDao().insert(CollectorMapper.fromRestDtoListToCollectorsEntity(collectors))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getDetailedCollectorById(collectorId: Int): DetailCollector {
        val collector = getCollectorDTOById(collectorId)
        return  DetailCollector (
            id = collectorId,
            name = collector.name,
            email = collector.email,
            phone = collector.telephone
        )
    }
    
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getCollectorFavoritePerformerIds(collectorId: Int): List<Int> {
        return withContext(Dispatchers.IO) {
            val dateInMillis = Date().time
            val cacheKey = "${EXPIRATION_COLLECTOR_PERFORMERS_VALUE}_$collectorId"
            val expirationDate = VinylsDataStore.readLongProperty(context, cacheKey)
            
            if (dateInMillis < expirationDate) {
                return@withContext getCollectorFavoritePerformerIdsFromLocalStorage(collectorId)
            } else {
                if (NetworkValidation.isNetworkAvailable(context)) {
                    try {
                        val performerDTOs = vinylsApiService.getCollectorFavoritePerformers(collectorId)
                        
                        val performerIds = performerDTOs.map { it.id }
                        
                        updateCollectorFavoritePerformersLocalStorage(collectorId, performerIds)
                        
                        VinylsDataStore.writeLongProperty(context, cacheKey, dateInMillis + DELAY_IN_MILLIS)
                        
                        return@withContext performerIds
                    } catch (e: Exception) {
                        return@withContext getCollectorFavoritePerformerIdsFromLocalStorage(collectorId)
                    }
                } else {
                    return@withContext getCollectorFavoritePerformerIdsFromLocalStorage(collectorId)
                }
            }
        }
    }
    
    private fun getCollectorFavoritePerformerIdsFromLocalStorage(collectorId: Int): List<Int> {
        return vinylsDatabase.collectorsDao().getCollectorFavoritePerformerIds(collectorId)
    }
    
    private suspend fun updateCollectorFavoritePerformersLocalStorage(collectorId: Int, performerIds: List<Int>) {
        val favoritePerformers = performerIds.map { 
            CollectorFavoritePerformer(collectorId = collectorId, performerId = it)
        }
        vinylsDatabase.collectorsDao().replaceCollectorFavoritePerformers(collectorId, favoritePerformers)
    }
    
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getCollectorAlbumIds(collectorId: Int): List<Int> {
        return withContext(Dispatchers.IO) {
            val dateInMillis = Date().time
            val cacheKey = "${EXPIRATION_COLLECTOR_ALBUMS_VALUE}_$collectorId"
            val expirationDate = VinylsDataStore.readLongProperty(context, cacheKey)
            
            if (dateInMillis < expirationDate) {
                return@withContext getCollectorAlbumIdsFromLocalStorage(collectorId)
            } else {
                if (NetworkValidation.isNetworkAvailable(context)) {
                    try {
                        val collectorAlbumDTOs = vinylsApiService.getCollectorAlbums(collectorId)
                        
                        val collectorAlbums = collectorAlbumDTOs.map { dto ->
                            Pair(
                                dto.album.id,
                                CollectorAlbum(
                                    collectorId = collectorId,
                                    albumId = dto.album.id,
                                    price = dto.price,
                                    status = dto.status
                                )
                            )
                        }
                        
                        updateCollectorAlbumsLocalStorage(collectorId, collectorAlbums.map { it.second })
                        
                        VinylsDataStore.writeLongProperty(context, cacheKey, dateInMillis + DELAY_IN_MILLIS)
                        
                        return@withContext collectorAlbums.map { it.first }
                    } catch (e: Exception) {
                        return@withContext getCollectorAlbumIdsFromLocalStorage(collectorId)
                    }
                } else {
                    return@withContext getCollectorAlbumIdsFromLocalStorage(collectorId)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getCollectorDTOById(id: Int): CollectorDTO {
        return withContext(Dispatchers.IO) {
            val dateInMillis = Date().time
            val expirationDate =
                VinylsDataStore.readLongProperty(context, EXPIRATION_COLLECTOR_DATA_VALUE)
            val collector: CollectorDTO

            if (dateInMillis < expirationDate) {

                collector = getDetailedCollectorByIdFromLocalStorage(id)
            } else {

                if (NetworkValidation.isNetworkAvailable(context)) {

                    collector = vinylsApiService.getCollectorById(id)
                } else {
                    collector = getDetailedCollectorByIdFromLocalStorage(id)
                }
            }
            collector
        }
    }

    private fun getDetailedCollectorByIdFromLocalStorage(id: Int): CollectorDTO {
        val collector = vinylsDatabase.collectorsDao().getCollectorById(id)
        return CollectorMapper.fromCollectorEntityToDTO(collector)
    }
    
    private fun getCollectorAlbumIdsFromLocalStorage(collectorId: Int): List<Int> {
        return vinylsDatabase.collectorsDao().getCollectorAlbumIds(collectorId)
    }
    
    private suspend fun updateCollectorAlbumsLocalStorage(collectorId: Int, collectorAlbums: List<CollectorAlbum>) {
        vinylsDatabase.collectorsDao().replaceCollectorAlbums(collectorId, collectorAlbums)
    }
}