package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.Performer as PerformerDTO
import co.edu.uniandes.miso.vinilos.model.domain.Album

/**
 * Mapper class to convert between AlbumDTO and Album domain models
 */
class AlbumMapper {
    
    companion object {
        
        /**
         * Converts an AlbumDTO to a domain Album
         */
        fun fromDto(albumDTO: AlbumDTO): Album {
            return Album(
                id = albumDTO.id,
                name = albumDTO.name,
                cover = albumDTO.cover,
                releaseDate = albumDTO.releaseDate,
                description = albumDTO.description,
                genre = albumDTO.genre,
                recordLabel = albumDTO.recordLabel,
                performer = if (albumDTO.performers.isNotEmpty()) albumDTO.performers[0].name else ""
            )
        }
        
        /**
         * Converts a domain Album to an AlbumDTO
         */
        fun toDto(album: Album): AlbumDTO {
            return AlbumDTO(
                id = album.id,
                name = album.name,
                cover = album.cover,
                releaseDate = album.releaseDate,
                description = album.description,
                genre = album.genre,
                recordLabel = album.recordLabel,
                tracks = emptyList(),
                performers = if (album.performer.isNotEmpty()) 
                    listOf(PerformerDTO(0, album.performer, "", "", null, null)) 
                    else emptyList(),
                comments = emptyList()
            )
        }
        
        /**
         * Converts a list of AlbumDTOs to a list of domain Albums
         */
        fun fromDtoList(albumDTOs: List<AlbumDTO>): List<Album> {
            return albumDTOs.map { fromDto(it) }
        }
        
        /**
         * Converts a list of domain Albums to a list of AlbumDTOs
         */
        fun toDtoList(albums: List<Album>): List<AlbumDTO> {
            return albums.map { toDto(it) }
        }
    }
} 