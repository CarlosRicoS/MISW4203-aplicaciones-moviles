package co.edu.uniandes.miso.vinilos.viewmodel.album

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val albumsRepository: VinylsAlbumsRepository
) : ViewModel() {

    private val _album = MutableLiveData<DetailAlbum>()
    val album: LiveData<DetailAlbum> = _album

    private val _comments = MutableLiveData<List<DetailComment>>()
    val comments: LiveData<List<DetailComment>> = _comments

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadAlbumDetail(albumId: Int) {
        viewModelScope.launch {
            try {
                val albumDetail = albumsRepository.getDetailedAlbumById(albumId)
                _album.value = albumDetail
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun addComment(content: String, rating: Int, idCollector: Int) {

        viewModelScope.launch {

            try {
                albumsRepository.addComment(album.value!!.id, content, rating, idCollector)
                loadCommentDetail(album.value!!.id)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}