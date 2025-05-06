package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CollectorDTO
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector

class CollectorMapper {

    companion object {

        fun fromRestDtoToSimplifiedCollector(collectorDTO: CollectorDTO): SimplifiedCollector {
            return SimplifiedCollector(
                collectorDTO.id, collectorDTO.name, collectorDTO.email
            )
        }

        fun fromRestDtoListSimplifiedCollectors(collectorDTOs: List<CollectorDTO>): List<SimplifiedCollector> {
            return collectorDTOs.map { fromRestDtoToSimplifiedCollector(it) }
        }
    }
}