package co.edu.uniandes.miso.vinilos.model.repository

import android.content.Context
import android.util.Log
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector
import co.edu.uniandes.miso.vinilos.model.settings.VinylsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val APP_USER_ID = "APP_USER_ID"

class VinylsUserSelectionRepository @Inject constructor(
    @ApplicationContext private val context: Context
){

    fun setCollectorAsAppUser(collector:SimplifiedCollector) {

        Log.i("algo", "collector as user")
        VinylsDataStore.writeLongProperty(context, APP_USER_ID, collector.id.toLong())
    }

    fun setRegularUserAsAppUser() {

        Log.i("algo", "Regular User")
        VinylsDataStore.writeLongProperty(context, APP_USER_ID, -1L)
    }
}