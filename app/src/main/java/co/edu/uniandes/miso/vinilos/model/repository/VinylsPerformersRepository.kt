package co.edu.uniandes.miso.vinilos.model.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.NetworkValidation
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.data.sqlite.VinylRoomDatabase
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.mapper.PerformerMapper
import co.edu.uniandes.miso.vinilos.model.settings.VinylsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

private const val DELAY_IN_MILLIS = 1000 * 60 * 10
private const val EXPIRATION_PERFORMER_DATA_VALUE = "EXPIRATION_PERFORMER_DATA_VALUE"

class VinylsPerformersRepository @Inject constructor(
    private val vinylsDatabase: VinylRoomDatabase,
    @ApplicationContext private val context: Context
) {

    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getSimplifiedPerformers(): List<SimplifiedPerformer> {
        return withContext(Dispatchers.IO) {

            getPerformersFromWebService()
        }
    }

    // TODO This code can only be included when the album repository stops deleting all the performers.
//    @RequiresApi(Build.VERSION_CODES.M)
//    suspend fun getSimplifiedPerformers(): List<SimplifiedPerformer> {
//        return withContext(Dispatchers.IO) {
//
//            val dateInMillis = Date().time
//            val expirationDate =
//                VinylsDataStore.readLongProperty(context, EXPIRATION_PERFORMER_DATA_VALUE)
//            val performersToReturn: List<SimplifiedPerformer>
//            if (dateInMillis < expirationDate) {
//
//                performersToReturn = getSimplifiedPerformersFromLocalStorage()
//            } else {
//
//                if (NetworkValidation.isNetworkAvailable(context)) {
//
//                    val performers = getPerformersFromWebService()
//                    updatePerformersLocalStorage(performers)
//                    VinylsDataStore.writeLongProperty(
//                        context,
//                        EXPIRATION_PERFORMER_DATA_VALUE,
//                        dateInMillis + (DELAY_IN_MILLIS)
//                    )
//                    performersToReturn = performers
//                } else {
//                    performersToReturn = getSimplifiedPerformersFromLocalStorage()
//                }
//            }
//            performersToReturn
//        }
//    }

    private fun getSimplifiedPerformersFromLocalStorage() : List<SimplifiedPerformer> {

        val performers = vinylsDatabase.performersDao().getPerformers()
        return performers.map { PerformerMapper.fromEntityToSimplifiedPerformer(it) }
    }

    private suspend fun getPerformersFromWebService() : List<SimplifiedPerformer> {

        val bands = vinylsApiService.getBands().map { PerformerMapper.fromRestBandToSimplifiedPerformer(it) }
        val musicians = vinylsApiService.getMusicians().map { PerformerMapper.fromRestMusicianToSimplifiedPerformer(it) }
        return bands + musicians
    }

    private suspend fun updatePerformersLocalStorage(performers: List<SimplifiedPerformer>) {

        val entitiesToInsert = PerformerMapper.fromSimplifiedPerformerListToEntities(performers)
        vinylsDatabase.performersDao().deleteAll()
        vinylsDatabase.performersDao().insertAll(entitiesToInsert)
    }
}