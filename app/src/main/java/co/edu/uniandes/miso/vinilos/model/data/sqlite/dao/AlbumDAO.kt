package co.edu.uniandes.miso.vinilos.model.data.sqlite.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Album
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Comment
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Track

@Dao
interface AlbumDAO {

    @Query("SELECT * FROM album")
    fun getAlbums():List<Album>

    @Query("SELECT * FROM track")
    fun getTracks():List<Track>

    @Query("SELECT * FROM comment")
    fun getComments(): List<Comment>

    @Query("SELECT * FROM performer")
    fun getPerformers(): List<Performer>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbums(albums: List<Album>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPerformer(albums: List<Performer>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracks(albums: List<Track>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComments(albums: List<Comment>)

    @Query("DELETE FROM album")
    suspend fun deleteAlbums()

    @Query("DELETE FROM track")
    suspend fun deleteTracks()

    @Query("DELETE FROM comment")
    suspend fun deleteComments()

    @Query("DELETE FROM performer")
    suspend fun deletePerformers()

    @Query("SELECT * FROM album WHERE id = :albumId")
    fun getAlbumById(albumId: Int): Album

    @Query("SELECT * FROM track WHERE albumId = :albumId")
    fun getTracksByAlbumId(albumId: Int): List<Track>

    @Query("SELECT * FROM comment WHERE albumId = :albumId")
    fun getCommentsByAlbumId(albumId: Int): List<Comment>

    @Query("SELECT performer.id, performer.name, performer.image, performer.description, performer.type FROM album INNER JOIN performer ON album.performer = performer.id WHERE album.id = :albumId")
    fun getPerformerById(albumId: Int): List<Performer>

    @Transaction
    suspend fun getAllAlbums(): List<Album> {
        return getAlbums()
    }

    @Transaction
    suspend fun deleteAll() {
        deleteAlbums()
        deleteTracks()
        deleteComments()
        deletePerformers()
    }

    @Transaction
    suspend fun insertAll(albums: List<Album>, performers: List<Performer>, comments: List<Comment>, tracks: List<Track>) {
        insertAlbums(albums)
        insertPerformer(performers)
        insertTracks(tracks)
        insertComments(comments)
    }

    @Transaction
    suspend fun getAlbumWithId(albumId: Int): List<List<Any>> {
        return listOf(
            listOf(getAlbumById(albumId)),
            getPerformerById(albumId),
            getCommentsByAlbumId(albumId),
            getTracksByAlbumId(albumId)
        )
    }
}