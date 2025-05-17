package co.edu.uniandes.miso.vinilos.model.data.sqlite.entity

import androidx.room.*

@Entity(
    tableName = "collector_album",
    primaryKeys = ["collectorId", "albumId"],
    foreignKeys = [
        ForeignKey(
            entity = Collector::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CollectorAlbum(
    @ColumnInfo(name = "collectorId") val collectorId: Int,
    @ColumnInfo(name = "albumId") val albumId: Int,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "status") val status: String
) 