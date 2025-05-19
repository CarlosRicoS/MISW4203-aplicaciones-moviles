package co.edu.uniandes.miso.vinilos.model.data.rest.dto

data class CollectorDTO(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<CollectorCommentDTO>,
    val favoritePerformers: List<CollectorPerformerDTO>,
    val collectorAlbums: List<CollectorAlbumDTO>
)

data class CollectorCommentDTO(
    val id: Int,
    val description: String,
    val rating: Int
)

data class CollectorPerformerDTO(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String
)

data class CollectorAlbumDTO(

    val id: Int,
    val price: Int,
    val status: String,
    val album: AlbumDTO
)