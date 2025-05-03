package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CommentDTO
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment


/**
 * Mapper class to convert between CommentDTO and Comment domain models
 */
class CommentMapper {
    companion object {
        /**
         * Converts a CommentDTO to a domain DetailComment
         */
        fun fromRestDtoToDetailComment(commentDTO: CommentDTO): DetailComment {
            return DetailComment(
                id = commentDTO.id,
                description = commentDTO.description,
                rating = commentDTO.rating,
                collector = "Coleccionista" // TODO: Replace with actual performer data,
            )
        }

        /**
         * Converts a list of CommentDTOs to a list of domain DetailComments
         */
        fun fromRestDtoListDetailComments(commentDTOs: List<CommentDTO>): List<DetailComment> {
            return commentDTOs.map { fromRestDtoToDetailComment(it) }
        }
    }
}