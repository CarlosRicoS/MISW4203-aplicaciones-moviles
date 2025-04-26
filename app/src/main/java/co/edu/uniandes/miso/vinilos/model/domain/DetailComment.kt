package co.edu.uniandes.miso.vinilos.model.domain

data class DetailComment (
    val id: Int,
    val description: String,
    val rating: Int,
    val collector: String?
)