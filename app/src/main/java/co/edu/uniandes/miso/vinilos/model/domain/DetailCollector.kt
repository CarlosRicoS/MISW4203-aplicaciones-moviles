package co.edu.uniandes.miso.vinilos.model.domain

data class DetailCollector (
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val favoritePerformers: List<SimplifiedPerformer> = emptyList(),
    val collectedAlbums: List<SimplifiedAlbum> = emptyList()
)