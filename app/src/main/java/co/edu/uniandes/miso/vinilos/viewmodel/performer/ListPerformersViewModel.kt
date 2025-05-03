package co.edu.uniandes.miso.vinilos.viewmodel.performer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import kotlinx.coroutines.launch

class ListPerformersViewModel : ViewModel() {

    //private val albumsRepository = VinylsAlbumsRepository()

    private val _performers = MutableLiveData<List<SimplifiedPerformer>>()

    val performers: LiveData<List<SimplifiedPerformer>> get() = _performers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadPerformers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                val performersList = listOf(
                    SimplifiedPerformer(
                        id = 100,
                        name = "Rubén Blades Bellido de Luna",
                        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ruben_Blades_by_Gage_Skidmore.jpg/800px-Ruben_Blades_by_Gage_Skidmore.jpg",
                        description = "",
                        performerType = PerformerType.MUSICIAN
                    ),
                    SimplifiedPerformer(
                        id = 101,
                        name = "Queen",
                        image = "https://pm1.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg",
                        description = "",
                        performerType = PerformerType.BAND
                    ),

                )

                //val performersList = albumsRepository.getSimplifiedVinylsAlbums()
                _performers.value = performersList.sortedBy { it.name }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
                _performers.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}