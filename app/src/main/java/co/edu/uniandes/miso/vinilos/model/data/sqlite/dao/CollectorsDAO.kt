package co.edu.uniandes.miso.vinilos.model.data.sqlite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Collector

@Dao
interface CollectorsDAO {

    @Query("SELECT * FROM collector")
    fun getCollectors():List<Collector>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collectors: List<Collector>)

    @Query("DELETE FROM collector")
    suspend fun deleteAll()
}