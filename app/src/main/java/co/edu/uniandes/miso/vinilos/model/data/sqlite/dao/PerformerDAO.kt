package co.edu.uniandes.miso.vinilos.model.data.sqlite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer

@Dao
interface PerformerDAO {
    @Query("SELECT * FROM performer")
    fun getPerformers(): List<Performer>

    @Query("SELECT * FROM performer WHERE id = :id AND type = :type")
    fun getPerformerByIdAndType(id: Int, type: String): Performer

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(perfomer: Performer)

    @Query("DELETE FROM performer")
    suspend fun deleteAll()

    @Query("UPDATE performer SET type = :type WHERE id = :id")
    suspend fun updatePerformerType(id: Int, type: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(albums: List<Performer>)
}