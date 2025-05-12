package co.edu.uniandes.miso.vinilos.model.domain

enum class PerformerType {
    MUSICIAN, BAND
}

data class SimplifiedPerformer (
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val performerType: PerformerType?
)