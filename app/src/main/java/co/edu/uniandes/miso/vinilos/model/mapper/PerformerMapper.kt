package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.PerformerDTO
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
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
                performerType = null
            )
        }


        fun fromRestMusicianToSimplifiedPerformer(performerDTO: PerformerDTO): SimplifiedPerformer {
            return SimplifiedPerformer(
                id = performerDTO.id,
                name = performerDTO.name,
                image = performerDTO.image,
                description = performerDTO.description,
                performerType = null,
            )
        }

        fun fromRestBandToSimplifiedPerformer(performerDTO: PerformerDTO): SimplifiedPerformer {
            return SimplifiedPerformer(
                id = performerDTO.id,
                name = performerDTO.name,
                image = performerDTO.image,
                description = performerDTO.description,
                performerType = PerformerType.BAND
            )
        }
    }
}