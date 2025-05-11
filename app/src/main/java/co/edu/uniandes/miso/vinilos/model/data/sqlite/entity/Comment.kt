package co.edu.uniandes.miso.vinilos.model.data.sqlite.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class Comment (
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "albumId") val albumId: Int
)
