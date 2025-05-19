package co.edu.uniandes.miso.vinilos.model.domain

data class NewAlbum(
    val name: String,
    val cover: String,
    val performerId: Int,
    val performerType: PerformerType,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)