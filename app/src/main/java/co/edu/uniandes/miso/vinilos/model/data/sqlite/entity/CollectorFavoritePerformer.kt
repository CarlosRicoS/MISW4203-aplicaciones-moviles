package co.edu.uniandes.miso.vinilos.model.data.sqlite.entity

import androidx.room.*

@Entity(
    tableName = "collector_favorite_performer",
    primaryKeys = ["collectorId", "performerId"],
    foreignKeys = [
        ForeignKey(
            entity = Collector::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CollectorFavoritePerformer(
    @ColumnInfo(name = "collectorId") val collectorId: Int,
    @ColumnInfo(name = "performerId") val performerId: Int
) 