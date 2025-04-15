package co.edu.uniandes.miso.vinilos.model.data.rest.dto.Album

data class AlbumDTO(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track>,
    val performers: List<Performer>,
    val comments: List<Comment>
)