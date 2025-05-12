package co.edu.uniandes.miso.vinilos.model.data.sqlite.entity

import androidx.room.*

@Entity(tableName = "collector")
data class Collector (
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "telephone") val telephone: String,
    @ColumnInfo(name = "email") val email: String,
)