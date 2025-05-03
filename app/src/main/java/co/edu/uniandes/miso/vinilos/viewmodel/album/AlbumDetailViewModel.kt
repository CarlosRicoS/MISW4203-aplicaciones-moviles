package co.edu.uniandes.miso.vinilos.viewmodel.album

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import kotlinx.coroutines.launch

class AlbumDetailViewModel : ViewModel() {
    private val albumsRepository = VinylsAlbumsRepository()

    private val _album = MutableLiveData<DetailAlbum>()
    val album: LiveData<DetailAlbum> = _album

    private val _performer = MutableLiveData<SimplifiedPerformer>()
    val performer: LiveData<SimplifiedPerformer> = _performer

    private val _comments = MutableLiveData<List<DetailComment>>()
    val comments: LiveData<List<DetailComment>> = _comments

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAlbumDetail(albumId: Int) {
        viewModelScope.launch {
            try {
                val albumDetail = albumsRepository.getVinylsAlbumById(albumId)
                _album.value = albumDetail
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPerformerDetail(albumId: Int) {
        viewModelScope.launch {
            try {
                val performersData = albumsRepository.getVinylsAlbumPerformer(albumId)
                if (performersData != null) {
                    _performer.value = performersData!!
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCommentDetail(albumId: Int) {
        viewModelScope.launch {
            try {
                val commentsList = albumsRepository.getVinylsAlbumComments(albumId)
                _comments.value = commentsList
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
                _comments.value = emptyList()
            }
        }
    }
}