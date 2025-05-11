package co.edu.uniandes.miso.vinilos.model.data.sqlite.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class Track (
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "albumId") val albumId: Int
)
