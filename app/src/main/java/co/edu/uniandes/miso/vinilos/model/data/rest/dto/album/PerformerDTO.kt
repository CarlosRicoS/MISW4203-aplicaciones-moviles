package co.edu.uniandes.miso.vinilos.model.data.rest.dto.album

data class PerformerDTO(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String?,
    val creationDate: String?
)