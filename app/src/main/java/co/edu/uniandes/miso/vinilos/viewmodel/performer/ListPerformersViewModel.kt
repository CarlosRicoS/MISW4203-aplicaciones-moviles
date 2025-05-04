package co.edu.uniandes.miso.vinilos.viewmodel.performer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.repository.VinylsMusiciansRepository
import kotlinx.coroutines.launch

class ListPerformersViewModel : ViewModel() {

    private val performersRepository = VinylsMusiciansRepository()

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
                val performersList = performersRepository.getVinylsMusicians()
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