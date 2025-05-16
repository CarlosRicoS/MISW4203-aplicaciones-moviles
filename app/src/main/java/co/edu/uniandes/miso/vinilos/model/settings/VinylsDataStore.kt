package co.edu.uniandes.miso.vinilos.model.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VinylsDataStore() {

    companion object {

        fun readLongProperty(context: Context, key: String): Long {

            val longKey = longPreferencesKey(key)
            val longKeyFlow =
                context.dataStore.data.map { preferences -> preferences[longKey] ?: -1 }
            return runBlocking {

                val value = longKeyFlow.first()
                value
            }
        }

        fun writeLongProperty(context: Context, key: String, value: Long) {

            val longKey = longPreferencesKey(key)
            runBlocking {
                context.dataStore.edit { preferences ->
                    preferences[longKey] = value
                }
            }
        }
    }
}