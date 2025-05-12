package co.edu.uniandes.miso.vinilos.model.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.PerformerDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.NetworkValidation
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.data.sqlite.VinylRoomDatabase
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.mapper.PerformerMapper
import co.edu.uniandes.miso.vinilos.model.settings.VinylsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

private const val DELAY_IN_MILLIS = 1000 * 60 * 10
private const val EXPIRATION_MUSICIANS_DATA_VALUE = "EXPIRATION_MUSICIANS_DATA_VALUE"

class VinylsMusiciansRepository @Inject constructor(
    private val vinylsDatabase: VinylRoomDatabase,
    @ApplicationContext private val context: Context
){
    private val vinylsApiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getSimplifiedMusicians(): List<SimplifiedPerformer> {

        return withContext(Dispatchers.IO) {


            val dateInMillis = Date().time
            val expirationDate = VinylsDataStore.readLongProperty(context, EXPIRATION_MUSICIANS_DATA_VALUE)
            val musiciansToReturn: List<SimplifiedPerformer>
            if (dateInMillis < expirationDate) {

                musiciansToReturn = getVinylsPerformersFromLocalStorage()
            } else {

                if(NetworkValidation.isNetworkAvailable(context)) {

                    musiciansToReturn = getVinylsPerformersFromService()
                    VinylsDataStore.writeLongProperty(context, EXPIRATION_MUSICIANS_DATA_VALUE, dateInMillis + (DELAY_IN_MILLIS))
                }
                else
                {
                    musiciansToReturn = getVinylsPerformersFromLocalStorage()
                }
            }

            musiciansToReturn
        }
    }

    private suspend fun getVinylsPerformersFromService(): List<SimplifiedPerformer> {
        val musiciansDTO = vinylsApiService.getMusicians()
        updatePerformersTypeLocalStorage(musiciansDTO, PerformerType.MUSICIAN)
        val bandsDTO = vinylsApiService.getBands()
        updatePerformersTypeLocalStorage(bandsDTO, PerformerType.BAND)
        return (
            PerformerMapper.fromRestDtoListSimplifiedPerformers(musiciansDTO, PerformerType.MUSICIAN) +
            PerformerMapper.fromRestDtoListSimplifiedPerformers(bandsDTO, PerformerType.BAND)
        )
    }

    private suspend fun updatePerformersTypeLocalStorage(performers: List<PerformerDTO>, performerType: PerformerType) {
        performers.map {
            performer -> vinylsDatabase.performersDao()
                .updatePerformerType(performer.id, performerType.toString())
        }
    }

    private fun getVinylsPerformersFromLocalStorage(): List<SimplifiedPerformer> {
        val performers = vinylsDatabase.performersDao().getPerformers()

        return (
            getPerformerByType(performers, PerformerType.MUSICIAN) +
            getPerformerByType(performers, PerformerType.BAND)
        )
    }

    private fun getPerformerByType(performers: List<Performer>, performerType: PerformerType): List<SimplifiedPerformer> {
        return PerformerMapper.fromRestDtoListSimplifiedPerformers(
            performers.filter { it.type == performerType.toString() }
                .map{ performer -> PerformerMapper.fromPerformerEntityToDto(performer) },
            performerType
        )
    }
}