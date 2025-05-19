package co.edu.uniandes.miso.vinilos.viewmodel.album

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniandes.miso.vinilos.model.domain.NewAlbum
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.repository.VinylsAlbumsRepository
import co.edu.uniandes.miso.vinilos.model.repository.VinylsPerformersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class NewAlbumViewModel @Inject constructor(
    private val albumsRepository: VinylsAlbumsRepository,
    private val performerRepository: VinylsPerformersRepository
) : ViewModel() {
    data class Option(val id: String, val label: String)
    data class PerformerOption(val id: String, val label: String, val type: String)

    private val _allowedGenres = MutableLiveData<List<Option>>()
    val allowedGenres: LiveData<List<Option>> = _allowedGenres

    private val _allowedRecordLabels = MutableLiveData<List<Option>>()
    val allowedRecordLabels: LiveData<List<Option>> = _allowedRecordLabels

    private val _existingPerformers = MutableLiveData<List<PerformerOption>>()
    val existingPerformers: LiveData<List<PerformerOption>> = _existingPerformers

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    @RequiresApi(Build.VERSION_CODES.M)
    fun loadFormValues() {
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

    private fun getRecordLabels(): List<Option> {
        return listOf(
            Option("SONY", "Sony Music"),
            Option("EMI", "EMI"),
            Option("FUENTES", "Discos Fuentes"),
            Option("ELEKTRA", "Elektra"),
            Option("FANIA", "Fania Records")
        )
    }

    private fun getGenres(): List<Option> {
        return listOf(
            Option("CLASSICAL", "Classical"),
            Option("SALSA", "Salsa"),
            Option("ROCK", "Rock"),
            Option("FOLK", "Folk")
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getPerformers(): List<PerformerOption> {

        val performers = performerRepository.getSimplifiedPerformers()
        return performers.map {
            PerformerOption(
                id = it.id.toString(),
                label = it.name,
                type = it.performerType!!.name
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun saveNewAlbum(newAlbumValues: MutableMap<String, Any?>) {

        val newAlbum = NewAlbum(
            name = newAlbumValues["name"].toString(),
            cover = newAlbumValues["cover"].toString(),
            performerId = (newAlbumValues["performer"] as Map<String, String>)["id"]!!.toInt(),
            performerType = PerformerType.valueOf((newAlbumValues["performer"] as Map<String, String>)["type"].toString()),
            releaseDate = formatDate(newAlbumValues["releaseDate"].toString()),
            description = newAlbumValues["description"].toString(),
            genre = newAlbumValues["genre"].toString(),
            recordLabel = newAlbumValues["recordLabel"].toString(),
        )

        albumsRepository.createAlbum(newAlbum)
    }
}