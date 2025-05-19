package co.edu.uniandes.miso.vinilos.model.data.sqlite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Collector
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.CollectorAlbum
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.CollectorFavoritePerformer

@Dao
interface CollectorsDAO {

    @Query("SELECT * FROM collector")
    fun getCollectors(): List<Collector>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collectors: List<Collector>)

    @Query("DELETE FROM collector")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM collector_favorite_performer WHERE collectorId = :collectorId")
    fun getCollectorFavoritePerformers(collectorId: Int): List<CollectorFavoritePerformer>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectorFavoritePerformers(favoritePerformers: List<CollectorFavoritePerformer>)
    
    @Query("DELETE FROM collector_favorite_performer WHERE collectorId = :collectorId")
    suspend fun deleteCollectorFavoritePerformers(collectorId: Int)
    
    @Query("SELECT performerId FROM collector_favorite_performer WHERE collectorId = :collectorId")
    fun getCollectorFavoritePerformerIds(collectorId: Int): List<Int>
    
    @Query("SELECT * FROM collector_album WHERE collectorId = :collectorId")
    fun getCollectorAlbums(collectorId: Int): List<CollectorAlbum>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectorAlbums(collectorAlbums: List<CollectorAlbum>)
    
    @Query("DELETE FROM collector_album WHERE collectorId = :collectorId")
    suspend fun deleteCollectorAlbums(collectorId: Int)
    
    @Query("SELECT albumId FROM collector_album WHERE collectorId = :collectorId")
    fun getCollectorAlbumIds(collectorId: Int): List<Int>
    
    @Transaction
    suspend fun replaceCollectorFavoritePerformers(collectorId: Int, favoritePerformers: List<CollectorFavoritePerformer>) {
        deleteCollectorFavoritePerformers(collectorId)
        insertCollectorFavoritePerformers(favoritePerformers)
    }
    
    @Transaction
    suspend fun replaceCollectorAlbums(collectorId: Int, collectorAlbums: List<CollectorAlbum>) {
        deleteCollectorAlbums(collectorId)
        insertCollectorAlbums(collectorAlbums)
    }
}