package co.edu.uniandes.miso.vinilos.viewmodel.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import kotlinx.coroutines.launch

class ListAlbumsViewModel : ViewModel() {

    private val albumsRepository = VinylsAlbumsRepository()
    
    private val _albums = MutableLiveData<List<SimplifiedAlbum>>()
    
    val albums: LiveData<List<SimplifiedAlbum>> get() = _albums
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadAlbums() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val albumsList = albumsRepository.getSimplifiedVinylsAlbums()
                _albums.value = albumsList
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
                _albums.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}