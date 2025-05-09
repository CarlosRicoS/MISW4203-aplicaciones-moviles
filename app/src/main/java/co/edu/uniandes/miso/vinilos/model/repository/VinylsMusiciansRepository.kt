package co.edu.uniandes.miso.vinilos.model.repository

import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.mapper.PerformerMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VinylsMusiciansRepository {
    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    suspend fun getVinylsMusicians(): List<SimplifiedPerformer> {
        return withContext(Dispatchers.IO) {
            val musiciansDTO = vinylsApiService.getMusicians()
            val bandsDTO = vinylsApiService.getBands()
            (
                PerformerMapper.fromRestDtoListSimplifiedPerformers(musiciansDTO, PerformerType.MUSICIAN) +
                PerformerMapper.fromRestDtoListSimplifiedPerformers(bandsDTO, PerformerType.BAND)
            )
        }
    }
}