package co.edu.uniandes.miso.vinilos.model.mapper

import co.edu.uniandes.miso.vinilos.model.data.rest.dto.CollectorDTO
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Collector
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

        fun fromCollectorListEntityToDTO(collectors: List<Collector>): List<CollectorDTO> {
            return collectors.map { fromCollectorEntityToDTO(it) }
        }

        fun fromCollectorEntityToDTO(collector: Collector): CollectorDTO {
            return CollectorDTO(
                collector.id,
                collector.name,
                collector.telephone,
                collector.email,
                listOf(),
                listOf(),
                listOf(),
            )
        }

        fun fromRestDtoListToCollectorsEntity(collectors: List<CollectorDTO>): List<Collector> {
            return collectors.map { collector ->
                Collector(
                    collector.id,
                    collector.name,
                    collector.telephone,
                    collector.email
                )
            }
        }
    }
}