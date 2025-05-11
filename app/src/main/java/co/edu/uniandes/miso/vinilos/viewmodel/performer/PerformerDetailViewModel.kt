package co.edu.uniandes.miso.vinilos.viewmodel.performer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformerDetailViewModel @Inject constructor(
    private val albumsRepository: VinylsAlbumsRepository
) : ViewModel() {

    private val _performer = MutableLiveData<SimplifiedPerformer>()
    val performer: LiveData<SimplifiedPerformer> = _performer

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPerformerDetail(performerId: Int, performerType: PerformerType) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val performerData = albumsRepository.getVinylsAlbumPerformer(performerId, performerType)
                if (performerData != null) {
                    _performer.value = performerData!!
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPerformerDetailByAlbumId(albumId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val performersData = albumsRepository.getVinylsAlbumPerformerByAlbumId(albumId)
                if (performersData != null) {
                    _performer.value = performersData!!
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

}