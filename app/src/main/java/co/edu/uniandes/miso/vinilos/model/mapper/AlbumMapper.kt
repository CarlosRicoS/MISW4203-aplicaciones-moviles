package co.edu.uniandes.miso.vinilos.model.mapper

import android.os.Build
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.AlbumDTO
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Album
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Comment
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Track
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import java.time.LocalDate
import java.time.OffsetDateTime
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

        fun fromAlbumListEntityToDTO(albums: List<Album>): List<SimplifiedAlbum> {
            return albums.fold(mutableListOf()) { acc, album ->
                val albumTransformed = SimplifiedAlbum(
                    id = album.id,
                    name = album.name,
                    cover = album.cover,
                    author = album.author,
                )
                acc.add(albumTransformed)
                acc
            }
        }

        fun fromAlbumEntityToDTO(
            albums: List<Album>,
            performers: List<Performer>,
            comments: List<Comment>,
            tracks: List<Track>
        ): AlbumDTO {
            return AlbumDTO(
                id = albums[0].id,
                name = albums[0].name,
                cover = albums[0].cover,
                releaseDate = albums[0].releaseDate,
                description = albums[0].description,
                genre = albums[0].genre,
                recordLabel = albums[0].recordLabel,
                performers = performers.map { performer -> PerformerMapper.fromPerformerEntityToDto(performer) },
                comments = comments.map { comment -> CommentMapper.fromCommentEntityToDto(comment) },
                tracks = tracks.map { track ->
                    co.edu.uniandes.miso.vinilos.model.data.rest.dto.Track(
                        id = track.id,
                        name = track.name,
                        duration = track.duration
                    )
                }
            )
        }

        fun fromAlbumDTOToEntity(albums: List<AlbumDTO>): List<List<Any>> {
            var comments = mutableListOf<Comment>()
            var performers = mutableListOf<Performer>()
            var tracks = mutableListOf<Track>()

            val albums = albums.map {
                album ->
                album.comments.map {
                    comment ->
                    if (!comments.any { c -> c.id == comment.id }) {
                        comments.add(Comment(comment.id, comment.description, comment.rating, album.id))
                    }
                }
                album.performers.map {
                    performer ->
                    if (!performers.any { p -> p.id == performer.id }) {
                        performers.add(Performer(performer.id, performer.name, performer.image, performer.description, null))
                    }
                }
                album.tracks.map {
                    track ->
                    if (!tracks.any { t -> t.id == track.id }) {
                        tracks.add(Track(track.id, track.name, track.duration, album.id))
                    }
                }
                Album (
                    id = album.id,
                    name = album.name,
                    cover = album.cover,
                    releaseDate = album.releaseDate,
                    description = album.description,
                    genre = album.genre,
                    recordLabel = album.recordLabel,
                    performerId = album.performers[0].id,
                    author = album.performers[0].name
                )
            }

            return listOf(albums, performers, comments, tracks)
        }
    }
}
