package co.edu.uniandes.miso.vinilos.viewmodel.album

import androidx.lifecycle.ViewModel
import co.edu.uniandes.miso.vinilos.model.domain.Album
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository

class ListAlbumsViewModel : ViewModel() {

    private val albumsRepository = VinylsAlbumsRepository()

    private var albums: MutableList<Album> = mutableListOf()

    suspend fun loadAlbums() {
        albums.clear()
        albums.addAll(albumsRepository.getVinylsAlbums())
    }

    fun getAlbums(): List<Album> {
        return albums
    }
}