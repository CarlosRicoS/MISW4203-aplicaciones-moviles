package co.edu.uniandes.miso.vinilos.viewmodel.collector

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import co.edu.uniandes.miso.vinilos.model.repository.VinylsCollectorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCollectorsViewModel @Inject constructor(
    private val collectorRepository: VinylsCollectorsRepository
): ViewModel() {

    private val _collectors = MutableLiveData<List<SimplifiedCollector>>()

    val collectors: LiveData<List<SimplifiedCollector>> get() = _collectors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadCollectors() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                val collectorData = collectorRepository.getSimplifiedVinylsCollectors()
                _collectors.value = collectorData.sortedBy { it.name }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
                _collectors.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}