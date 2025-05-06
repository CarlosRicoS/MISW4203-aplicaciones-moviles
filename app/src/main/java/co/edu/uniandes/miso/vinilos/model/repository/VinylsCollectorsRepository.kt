package co.edu.uniandes.miso.vinilos.model.repository

import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector
import co.edu.uniandes.miso.vinilos.model.mapper.CollectorMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VinylsCollectorsRepository {

    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    suspend fun getSimplifiedVinylsCollectors(): List<SimplifiedCollector> {
        return withContext(Dispatchers.IO) {
            val collectors = vinylsApiService.getCollectors()
            CollectorMapper.fromRestDtoListSimplifiedCollectors(collectors)
        }
    }
}