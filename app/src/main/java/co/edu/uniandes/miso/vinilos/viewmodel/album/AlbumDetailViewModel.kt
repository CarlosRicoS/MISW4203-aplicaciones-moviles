package co.edu.uniandes.miso.vinilos.viewmodel.album

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.Comment
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.Performer
import java.time.LocalDate

class AlbumDetailViewModel : ViewModel() {
    private val _album = MutableLiveData<AlbumDTO>()
    val album: LiveData<AlbumDTO> = _album

    private val _performers = MutableLiveData<List<Performer>>()
    val performers: LiveData<List<Performer>> = _performers

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAlbumDetail(albumId: Int) {
        var albumDetail = getAlbumDetail(albumId)
        val year = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(albumDetail.releaseDate).year.toString()
        } else ""
        _album.value = AlbumDTO(
            id = albumDetail.id,
            name = albumDetail.name,
            cover = albumDetail.cover,
            releaseDate = year,
            description = albumDetail.description,
            genre = albumDetail.genre,
            recordLabel = albumDetail.recordLabel,
            tracks = albumDetail.tracks,
            performers = albumDetail.performers,
            comments = albumDetail.comments
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPerformerDetail(albumId: Int) {
        _performers.value = getAlbumPerformers(albumId).map { performer ->
            val year = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.parse(performer.creationDate).year.toString()
            } else ""
            Performer(
                id = performer.id,
                name = performer.name,
                image = performer.image,
                description = performer.description,
                birthDate = year,
                creationDate = year,
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCommentDetail(albumId: Int) {
        _comments.value = getAlbumComments(albumId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAlbumDetail(albumId: Int): AlbumDTO {
        return AlbumDTO(
            id = albumId,
            name = "Album Name",
            cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
            releaseDate = LocalDate.of(2023, 1, 1).toString(),
            description = "Nevermind es el segundo álbum de estudio de la banda estadounidense de grunge Nirvana, publicado el 24 de septiembre de 1991.",
            genre = "Rock",
            recordLabel = "EMI",
            tracks = emptyList(),
            performers = emptyList(),
            comments = emptyList()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAlbumPerformers(albumId: Int): List<Performer> {
        return listOf(
            Performer(
                id = 1,
                name = "Performer Name",
                image = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
                description = "Nirvana fue una banda de rock estadounidense formada en Aberdeen (Washington), en 1987. Fundada por el cantante y guitarrista Kurt Cobain y el bajista Krist Novoselic, la banda pasó por una sucesión de bateristas, sobre todo Chad Channing, y luego reclutó a Dave Grohl en 1990. El éxito de la banda popularizó el rock alternativo y, a menudo, se los mencionaba como la banda proa de la Generación X.",
                birthDate = LocalDate.of(2023, 1, 1).toString(),
                creationDate = LocalDate.of(2023, 1, 1).toString(),
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAlbumComments(albumId: Int): List<Comment> {
        return listOf(
            Comment(
                id = 1,
                description = "This is a comment",
                rating = 5,
            ),
            Comment(
                id = 2,
                description = "This is a comment 2 kljashdkljhfdskjhfgkjsdhfkldhskdf as flkjhsdklfgjhfs sdlkf lksdnjf ljnsaf sdlkfj dsljf lksdjf slfdk jslkfj ldskjlfd jslfld jslkf ldfkjfl vsxdlf kfjdbglfknjh de",
                rating = -4,
            ),
            Comment(
                id = 3,
                description = "This is a comment 2 kljashdkljhfdskjhfgkjsdhfkldhskdf as flkjhsdklfgjhfs sdlkf lksdnjf ljnsaf sdlkfj dsljf lksdjf slfdk jslkfj ldskjlfd jslfld jslkf ldfkjfl vsxdlf kfjdbglfknjh de",
                rating = 10,
            )
        )
    }
}