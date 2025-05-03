package co.edu.uniandes.miso.vinilos.viewmodel.collector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector
import kotlinx.coroutines.launch

class ListCollectorsViewModel : ViewModel() {
    //private val albumsRepository = VinylsAlbumsRepository()

    private val _collectors = MutableLiveData<List<SimplifiedCollector>>()

    val collectors: LiveData<List<SimplifiedCollector>> get() = _collectors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadCollectors() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                val collectorsList = listOf(
                    SimplifiedCollector(
                        id = 100,
                        name = "Manolo Bellon",
                        email = "manollo@caracol.com.co",
                    ),
                    SimplifiedCollector(
                        id = 101,
                        name = "Jaime Monsalve",
                        email = "jmonsalve@rtvc.com.co",
                    ),

                    )

                //val collectorsList = albumsRepository.getSimplifiedVinylsAlbums()
                _collectors.value = collectorsList.sortedBy { it.name }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
                _collectors.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}