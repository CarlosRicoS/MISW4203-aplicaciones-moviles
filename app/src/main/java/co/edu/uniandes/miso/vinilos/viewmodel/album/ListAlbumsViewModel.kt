package co.edu.uniandes.miso.vinilos.viewmodel.album

import androidx.lifecycle.ViewModel
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsApiService
import co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter.VinylsServiceAdapter
import co.edu.uniandes.miso.vinilos.model.repository.NetworkVinylsAlbumsRepository
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository

class ListAlbumsViewModel : ViewModel() {

    private val apiService: VinylsApiService = VinylsServiceAdapter.instance.vinylsService

    private val albumsRepository: VinylsAlbumsRepository = NetworkVinylsAlbumsRepository(apiService)

    private var albums: MutableList<AlbumDTO> = mutableListOf()

    suspend fun loadAlbums() {
        albums.clear()
        albums.addAll(albumsRepository.getVinylsAlbums())
    }

    fun getAlbums(): List<AlbumDTO> {
        return albums
    }

}