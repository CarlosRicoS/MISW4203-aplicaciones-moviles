package co.edu.uniandes.miso.vinilos.model.repository

import android.os.Build
import androidx.annotation.RequiresApi
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.mapper.PerformerMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VinylsPerformersRepository @Inject constructor(
) {

    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getSimplifiedPerformers(): List<SimplifiedPerformer> {
        return withContext(Dispatchers.IO) {

            getPerformersFromWebService()
        }
    }

    private suspend fun getPerformersFromWebService(): List<SimplifiedPerformer> {

        val bands = vinylsApiService.getBands()
            .map { PerformerMapper.fromRestBandToSimplifiedPerformer(it) }
        val musicians = vinylsApiService.getMusicians()
            .map { PerformerMapper.fromRestMusicianToSimplifiedPerformer(it) }
        return bands + musicians
    }
}