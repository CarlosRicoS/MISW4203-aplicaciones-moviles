package co.edu.uniandes.miso.vinilos.viewmodel.performer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.repository.VinylsMusiciansRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPerformersViewModel @Inject constructor(
    private val performersRepository: VinylsMusiciansRepository
) : ViewModel() {

    private val _performers = MutableLiveData<List<SimplifiedPerformer>>()

    val performers: LiveData<List<SimplifiedPerformer>> get() = _performers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    @RequiresApi(Build.VERSION_CODES.M)
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