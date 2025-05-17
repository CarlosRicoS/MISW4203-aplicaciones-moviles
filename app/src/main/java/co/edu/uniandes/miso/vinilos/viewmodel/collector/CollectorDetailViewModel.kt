package co.edu.uniandes.miso.vinilos.viewmodel.collector

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.DetailCollector
import co.edu.uniandes.miso.vinilos.model.repository.VinylsCollectorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectorDetailViewModel @Inject constructor(
    private val collectorRepository: VinylsCollectorsRepository
): ViewModel() {

    private val _collector = MutableLiveData<DetailCollector>()
    val collector: LiveData<DetailCollector> = _collector

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            try {
                val collectorDetail = collectorRepository.getDetailedCollectorById(collectorId)
                _collector.value = collectorDetail
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}