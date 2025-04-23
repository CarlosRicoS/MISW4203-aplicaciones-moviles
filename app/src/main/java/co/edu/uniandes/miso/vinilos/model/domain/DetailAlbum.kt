package co.edu.uniandes.miso.vinilos.model.domain

data class DetailAlbum(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
)