package co.edu.uniandes.miso.vinilos.model.data.rest.dto

data class PerformerDTO(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val albums: List<AlbumDTO>
)