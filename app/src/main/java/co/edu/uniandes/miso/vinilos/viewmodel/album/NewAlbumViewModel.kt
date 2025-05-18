package co.edu.uniandes.miso.vinilos.viewmodel.album

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class NewAlbumViewModel @Inject constructor(
    private val albumsRepository: VinylsAlbumsRepository
): ViewModel() {
    data class Option(val id: Int, val label: String)

    private val _allowedGenres = MutableLiveData<List<Option>>()
    val allowedGenres: LiveData<List<Option>> = _allowedGenres

    private val _allowedRecordLabels = MutableLiveData<List<Option>>()
    val allowedRecordLabels: LiveData<List<Option>> = _allowedRecordLabels

    private val _existingPerformers = MutableLiveData<List<Option>>()
    val existingPerformers: LiveData<List<Option>> = _existingPerformers

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadFormValues () {
        viewModelScope.launch {
            try {
                _allowedGenres.value = getGenres()
                _allowedRecordLabels.value = getRecordLabels()
                _existingPerformers.value = getPerformers()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun formatDate(date: String?): String {
        if (date.isNullOrBlank()) return ""
        val inputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val parsedDate = inputFormat.parse(date)

        val releaseDateIsoString = parsedDate?.let { outputFormat.format(it) }
        return releaseDateIsoString.toString()
    }

    //TODO: Obetener datos de la API o local storage
    private fun getRecordLabels(): List<Option> {
        return listOf(
            Option(1, "Sony Music"),
            Option(2, "Universal Music Group"),
            Option(3, "Warner Music Group")
        )

    }

    //TODO: Obetener datos de la API o local storage
    private fun getGenres(): List<Option> {
        return listOf(
            Option(1, "Rock"),
            Option(2, "Pop"),
            Option(3, "Metal")
        )
    }

    //TODO: Obetener datos de la API o local storage
    private fun getPerformers(): List<Option> {
        return listOf(
            Option(1, "Anonymous"),
            Option(2, "John Doe"),
            Option(3, "Jane Smith")
        )
    }

    // TODO: Conectar con el repositorio
    @RequiresApi(Build.VERSION_CODES.N)
    fun saveNewAlbum(newAlbumValues: MutableMap<String, Any?>) {
        if (newAlbumValues["releaseDate"].toString().isNullOrBlank()){
            newAlbumValues["releaseDate"] = formatDate(newAlbumValues["releaseDate"].toString())
        }
        return
    }
}