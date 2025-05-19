package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.PerformerDTO
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer
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
                performerType = PerformerType.MUSICIAN
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

        fun fromRestDtoListSimplifiedPerformers(performersDTO: List<PerformerDTO>, performerType: PerformerType): List<SimplifiedPerformer> {
            return performersDTO.map {
                if (performerType == PerformerType.MUSICIAN)
                    fromRestMusicianToSimplifiedPerformer(it)
                else fromRestBandToSimplifiedPerformer(it)
            }
        }

        fun fromPerformerEntityToDto(performer: Performer): PerformerDTO {
            return PerformerDTO(
                id = performer.id,
                name = performer.name,
                image = performer.image,
                description = performer.description,
                albums = listOf()
            )
        }

        fun fromSimplifiedPerformerToEntity(simplifiedPerformer: SimplifiedPerformer): Performer {

            return Performer(
                id = simplifiedPerformer.id,
                name = simplifiedPerformer.name,
                description = simplifiedPerformer.description,
                image = simplifiedPerformer.image,
                type = simplifiedPerformer.performerType!!.name
            )
        }

        fun fromSimplifiedPerformerListToEntities(simplifiedPerformers: List<SimplifiedPerformer>): List<Performer> {
            return simplifiedPerformers.map {
                fromSimplifiedPerformerToEntity(it)
            }
        }

        fun fromEntityToSimplifiedPerformer(entity: Performer): SimplifiedPerformer {

            return SimplifiedPerformer(
                id = entity.id,
                name = entity.name,
                image =  entity.image,
                description = entity.description,
                performerType = PerformerType.valueOf(entity.type!!)
            )
        }
    }
}