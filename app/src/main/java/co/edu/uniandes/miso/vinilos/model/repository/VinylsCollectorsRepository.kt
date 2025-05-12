package co.edu.uniandes.miso.vinilos.model.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CollectorDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.NetworkValidation
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.data.sqlite.VinylRoomDatabase
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
}