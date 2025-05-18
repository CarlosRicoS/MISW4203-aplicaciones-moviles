package co.edu.uniandes.miso.vinilos.model.data.rest.dto

data class CreateAlbumRequest (
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)
