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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VinylsCollectorsRepository @Inject constructor(
    private val vinylsDatabase: VinylRoomDatabase,
    @ApplicationContext private val context: Context
) {

    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getSimplifiedVinylsCollectors(): List<SimplifiedCollector> {
        return withContext(Dispatchers.IO) {
            val collectors = if (NetworkValidation.isNetworkAvailable(context))
                vinylsApiService.getCollectors() else getVinylsCollectorsFromLocalStorage()
            updateCollectorLocalStorage(collectors)
            CollectorMapper.fromRestDtoListSimplifiedCollectors(collectors)
        }
    }

    private fun getVinylsCollectorsFromLocalStorage(): List<CollectorDTO> {
        val collectors = vinylsDatabase.collectorsDao().getCollectors()
        return CollectorMapper.fromCollectorListEntityToDTO(collectors)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun updateCollectorLocalStorage(collectors: List<CollectorDTO>) {
        if (!NetworkValidation.isNetworkAvailable(context)) return
        // @TODO add validation with data expiration
        vinylsDatabase.collectorsDao().deleteAll()
        vinylsDatabase.collectorsDao().insert(CollectorMapper.fromRestDtoListToCollectorsEntity(collectors))
    }
}