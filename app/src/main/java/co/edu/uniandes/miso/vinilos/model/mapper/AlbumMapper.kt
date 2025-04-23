package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum

/**
 * Mapper class to convert between AlbumDTO and Album domain models
 */
class AlbumMapper {
    
    companion object {
        
        /**
         * Converts an AlbumDTO to a domain SimplifiedAlbum
         */
        fun fromRestDtoToSimplifiedAlbum(albumDTO: AlbumDTO): SimplifiedAlbum {
            return SimplifiedAlbum(
                id = albumDTO.id,
                name = albumDTO.name,
                cover = albumDTO.cover,
                author = if (albumDTO.performers.isNotEmpty()) albumDTO.performers[0].name else ""
            )
        }

        /**
         * Converts an AlbumDTO to a domain DetailAlbum
         */
        fun fromRestDtoToDetailAlbum(albumDTO: AlbumDTO): DetailAlbum {
            return DetailAlbum(
                id = albumDTO.id,
                name = albumDTO.name,
                cover = albumDTO.cover,
                releaseDate = albumDTO.releaseDate,
                description = albumDTO.description,
                genre = albumDTO.genre,
                recordLabel = albumDTO.recordLabel,
            )
        }

        /**
         * Converts a list of AlbumDTOs to a list of domain Simplified Albums
         */
        fun fromRestDtoListSimplifiedAlbums(albumDTOs: List<AlbumDTO>): List<SimplifiedAlbum> {
            return albumDTOs.map { fromRestDtoToSimplifiedAlbum(it) }
        }

        /**
         * Converts a list of AlbumDTOs to a list of domain Detail Albums
         */
        fun fromRestDtoListDetailAlbums(albumDTOs: List<AlbumDTO>): List<DetailAlbum> {
            return albumDTOs.map { fromRestDtoToDetailAlbum(it) }
        }

    }
} 