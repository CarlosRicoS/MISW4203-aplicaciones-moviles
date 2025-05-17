package co.edu.uniandes.miso.vinilos.viewmodel.collector

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.DetailCollector
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import co.edu.uniandes.miso.vinilos.model.repository.VinylsCollectorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectorDetailViewModel @Inject constructor(
    private val collectorRepository: VinylsCollectorsRepository,
    private val albumsRepository: VinylsAlbumsRepository
): ViewModel() {

    private val _collector = MutableLiveData<DetailCollector>()
    val collector: LiveData<DetailCollector> = _collector

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val collectorDetail = collectorRepository.getDetailedCollectorById(collectorId)
                
                val favoritePerformerIds = collectorRepository.getCollectorFavoritePerformerIds(collectorId)
                val albumIds = collectorRepository.getCollectorAlbumIds(collectorId)
                
                val performers = if (favoritePerformerIds.isNotEmpty()) {
                    loadPerformers(favoritePerformerIds)
                } else {
                    createDummyPerformers()
                }
                
                val albums = if (albumIds.isNotEmpty()) {
                    loadAlbums(albumIds)
                } else {
                    createDummyAlbums()
                }
                
                _collector.value = collectorDetail.copy(
                    favoritePerformers = performers,
                    collectedAlbums = albums
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun loadPerformers(performerIds: List<Int>): List<SimplifiedPerformer> {
        val performers = mutableListOf<SimplifiedPerformer>()
        
        for (id in performerIds) {
            try {
                val performer = albumsRepository.getVinylsAlbumPerformer(id, PerformerType.MUSICIAN)
                if (performer != null) {
                    performers.add(performer)
                    continue
                }
            } catch (e: Exception) {
                // If it fails as a musician, try as a band
            }
            
            try {
                val performer = albumsRepository.getVinylsAlbumPerformer(id, PerformerType.BAND)
                if (performer != null) {
                    performers.add(performer)
                }
            } catch (e: Exception) {
                // If both fail, continue to the next ID
            }
        }
        
        return performers
    }
    
    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun loadAlbums(albumIds: List<Int>): List<SimplifiedAlbum> {
        val albums = mutableListOf<SimplifiedAlbum>()
        
        for (id in albumIds) {
            try {
                val album = albumsRepository.getDetailedAlbumById(id)
                albums.add(
                    SimplifiedAlbum(
                        id = album.id,
                        name = album.name,
                        cover = album.cover,
                        author = album.performer?.name ?: ""
                    )
                )
            } catch (e: Exception) {
                // If the album loading fails, continue to the next ID
            }
        }
        
        return albums
    }
    
    private fun createDummyPerformers(): List<SimplifiedPerformer> {
        return listOf(
            SimplifiedPerformer(
                id = 1,
                name = "Rubén Blades Bellido de Luna",
                image = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ruben_Blades_by_Gage_Skidmore.jpg/800px-Ruben_Blades_by_Gage_Skidmore.jpg",
                description = "Rubén Blades Bellido de Luna es un cantante, compositor, músico, actor, abogado, político y activista panameño.",
                performerType = PerformerType.MUSICIAN
            ),
            SimplifiedPerformer(
                id = 2,
                name = "Queen",
                image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Queen_%E2%80%93_montagem_%E2%80%93_new.png/800px-Queen_%E2%80%93_montagem_%E2%80%93_new.png",
                description = "Queen es una banda británica de rock formada en 1970 en Londres.",
                performerType = PerformerType.BAND
            ),
            SimplifiedPerformer(
                id = 3,
                name = "Michael Jackson",
                image = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Michael_Jackson_1984.jpg/800px-Michael_Jackson_1984.jpg",
                description = "Michael Joseph Jackson fue un cantante, compositor y bailarín estadounidense.",
                performerType = PerformerType.MUSICIAN
            )
        )
    }
    
    private fun createDummyAlbums(): List<SimplifiedAlbum> {
        return listOf(
            SimplifiedAlbum(
                id = 1,
                name = "Buscando América",
                cover = "https://upload.wikimedia.org/wikipedia/en/3/32/Rul%C3%A9.jpg",
                author = "Rubén Blades Bellido de Luna"
            ),
            SimplifiedAlbum(
                id = 2,
                name = "A Night at the Opera",
                cover = "https://upload.wikimedia.org/wikipedia/en/4/4d/Queen_A_Night_At_The_Opera.png",
                author = "Queen"
            ),
            SimplifiedAlbum(
                id = 3,
                name = "Thriller",
                cover = "https://upload.wikimedia.org/wikipedia/en/5/55/Michael_Jackson_-_Thriller.png",
                author = "Michael Jackson"
            )
        )
    }
}