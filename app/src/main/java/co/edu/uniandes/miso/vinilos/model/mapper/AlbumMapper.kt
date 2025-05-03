package co.edu.uniandes.miso.vinilos.model.mapper

import android.os.Build
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
                releaseDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        // Try parsing as OffsetDateTime first (ISO 8601 with time and timezone)
                        val date = OffsetDateTime.parse(albumDTO.releaseDate)
                        date.year.toString()
                    } catch (e: DateTimeParseException) {
                        try {
                            // Fallback to LocalDate if only date portion is provided
                            val date = LocalDate.parse(albumDTO.releaseDate)
                            date.year.toString()
                        } catch (e: DateTimeParseException) {
                            // Just return the original string if all parsing fails
                            albumDTO.releaseDate
                        }
                    }
                } else albumDTO.releaseDate,
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