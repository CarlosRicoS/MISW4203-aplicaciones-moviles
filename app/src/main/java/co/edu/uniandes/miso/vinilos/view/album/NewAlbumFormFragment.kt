package co.edu.uniandes.miso.vinilos.view.album

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.NewAlbumFormBinding
import co.edu.uniandes.miso.vinilos.view.interfaces.ToolbarActionHandler
import co.edu.uniandes.miso.vinilos.viewmodel.album.NewAlbumViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewAlbumFormFragment : Fragment(), ToolbarActionHandler {

    private val newAlbumViewModel: NewAlbumViewModel by viewModels()
    private var _binding: NewAlbumFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar
    private var newAlbumResult: MutableMap<String, Any?> =  mutableMapOf<String, Any?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewAlbumFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePickerControl()
        setUpObservers()
        loadData()
        progressBar = binding.newAlbumProgressBar
        progressBar.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
        newAlbumViewModel.loadFormValues()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpObservers() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.new_album_title)

        newAlbumViewModel.allowedGenres.observe(viewLifecycleOwner) { genres ->
            setUpSelector(binding.genre, genres, "genre")
            progressBar.visibility = View.GONE
        }
        newAlbumViewModel.allowedRecordLabels.observe(viewLifecycleOwner) { recordLabels ->
            setUpSelector(binding.recordLabel, recordLabels, "recordLabel")
            progressBar.visibility = View.GONE
        }
        newAlbumViewModel.existingPerformers.observe(viewLifecycleOwner) { performers ->
            setUpSelectorForPerformerOptions(binding.performer, performers, "performer")
            progressBar.visibility = View.GONE
        }
        newAlbumViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setDatePickerControl() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.new_album_release_date_placeholder)
                .build()

        datePicker.addOnPositiveButtonClickListener {
            binding.releaseDate.setText(datePicker.headerText)
        }
        binding.releaseDate.setOnClickListener {
            datePicker.show(parentFragmentManager, "tag")
        }
    }

    private fun setUpSelector(dropdownView: MaterialAutoCompleteTextView,
                              items: List<NewAlbumViewModel.Option>, propertyName: String)
    {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            items.map { item -> item.label }
        )

        dropdownView.setAdapter(adapter)

        dropdownView.keyListener = null

        dropdownView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            newAlbumResult[propertyName] = selectedItem.label
        }
    }

    private fun setUpSelectorForPerformerOptions(dropdownView: MaterialAutoCompleteTextView,
                              items: List<NewAlbumViewModel.PerformerOption>, propertyName: String)
    {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            items.map { item -> item.label }
        )

        dropdownView.setAdapter(adapter)

        dropdownView.keyListener = null

        dropdownView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            newAlbumResult[propertyName] = mapOf(Pair("id", selectedItem.id), Pair("type", selectedItem.type)  )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onToolbarAction(): Boolean {

        newAlbumResult["name"] = binding.name.text.toString()
        newAlbumResult["cover"] = binding.cover.text.toString()
        newAlbumResult["releaseDate"] = binding.releaseDate.text.toString()
        newAlbumResult["description"] = binding.description.text.toString()

        return if(isFormValid()) {

            lifecycleScope.launch {

                newAlbumViewModel.saveNewAlbum(newAlbumResult)
                findNavController().navigate(R.id.albumsListFragment, null)
                Toast.makeText(context, "Agregado", Toast.LENGTH_LONG).show()
            }

            true
        }
        else
        {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isFormValid(): Boolean {
        return (
            isValidName() &&
            isValidCover() &&
            isValidReleaseDate() &&
            isValidDescription() &&
            isValidGenre() &&
            isValidRecordLabel() &&
            isValidPerformer()
        )
    }

    private fun isValidUrl(url: String): Boolean {
        return Regex("^(https?|ftp)://[\\w.-]+(?:\\.[\\w\\.-]+)+[/\\w\\.-]*(?:\\?[^\\s]*)?\$").matches(url)
    }

    private fun isValidField(condition: Boolean, binding: TextInputEditText, message: String): Boolean {
        if (condition) {
            binding.error = message
            return false
        }
        else {
            binding.error = null
            return true
        }
    }

    private fun isValidField(condition: Boolean, binding: MaterialAutoCompleteTextView, message: String): Boolean {
        if (condition) {
            binding.error = message
            return false
        }
        else {
            binding.error = null
            return true
        }
    }

    private fun isValidName(): Boolean {
        return isValidField(
            newAlbumResult["name"].toString().isBlank(),
            binding.name,
            getString(R.string.new_album_name_required)
        )
    }

    private fun isValidCover(): Boolean {
        return isValidField(
            !isValidUrl(newAlbumResult["cover"].toString()),
            binding.cover,
            getString(R.string.new_album_cover_required)
        )
    }

    private fun isValidReleaseDate(): Boolean {
        return isValidField(
            newAlbumResult["releaseDate"].toString().isBlank(),
            binding.releaseDate,
            getString(R.string.new_album_release_date_required)
        )
    }

    private fun isValidDescription(): Boolean {
        return isValidField(
            newAlbumResult["description"].toString().isBlank(),
            binding.description,
            getString(R.string.new_album_description_required)
        )
    }

    private fun isValidGenre(): Boolean {
        return isValidField(
            newAlbumResult["genre"] == null,
            binding.genre,
            getString(R.string.new_album_genre_required)
        )
    }

    private fun isValidRecordLabel(): Boolean {
        return isValidField(
            newAlbumResult["recordLabel"] == null,
            binding.recordLabel,
            getString(R.string.new_album_record_label_required)
        )
    }

    private fun isValidPerformer(): Boolean {
        return isValidField(
            newAlbumResult["performer"] == null,
            binding.performer,
            getString(R.string.new_album_performer_required)
        )
    }
}