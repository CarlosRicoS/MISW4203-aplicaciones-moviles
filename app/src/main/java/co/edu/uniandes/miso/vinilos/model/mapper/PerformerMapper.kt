package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.PerformerDTO
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer

/**
 * Mapper class to convert between PerformerDTO and Performer domain models
 */
class PerformerMapper {
    companion object {
        /**
         * Converts a PerformerDTO to a domain SimplifiedPerformer
         */
        fun fromRestDtoToSimplifiedPerformer(performerDTO: PerformerDTO): SimplifiedPerformer {
            return SimplifiedPerformer(
                id = performerDTO.id,
                name = performerDTO.name,
                image = performerDTO.image,
                description = performerDTO.description,
            )
        }
    }
}