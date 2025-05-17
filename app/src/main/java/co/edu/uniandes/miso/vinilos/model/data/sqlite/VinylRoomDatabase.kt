package co.edu.uniandes.miso.vinilos.model.data.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.edu.uniandes.miso.vinilos.model.data.sqlite.dao.AlbumDAO
import co.edu.uniandes.miso.vinilos.model.data.sqlite.dao.CollectorsDAO
import co.edu.uniandes.miso.vinilos.model.data.sqlite.dao.PerformerDAO
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Album
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Collector
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.CollectorAlbum
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.CollectorFavoritePerformer
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Performer
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Track
import co.edu.uniandes.miso.vinilos.model.data.sqlite.entity.Comment

@Database(
    entities = [
        Album::class,
        Collector::class,
        Track::class,
        Performer::class,
        Comment::class,
        CollectorFavoritePerformer::class,
        CollectorAlbum::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VinylRoomDatabase : RoomDatabase() {

    abstract fun collectorsDao(): CollectorsDAO
    abstract fun albumsDao(): AlbumDAO
    abstract fun performersDao(): PerformerDAO


    companion object {

        @Volatile
        private var INSTANCE: VinylRoomDatabase? = null

        fun getDatabase(context: Context): VinylRoomDatabase {
            val databaseName = "vinyls_database"
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinylRoomDatabase::class.java,
                    databaseName
                )
                .fallbackToDestructiveMigration() // Handle version upgrades by recreating the database
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}