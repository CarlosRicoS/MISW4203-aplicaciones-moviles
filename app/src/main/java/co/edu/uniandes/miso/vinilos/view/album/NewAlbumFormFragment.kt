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
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.MaterialAutoCompleteTextView

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
        progressBar = binding.collectorDetailProgressBar
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
            setUpSelector(binding.performer, performers, "performer")
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
            newAlbumResult[propertyName] = if (propertyName.equals("performer")) selectedItem.id else selectedItem.label
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onToolbarAction(): Boolean {
        //@TODO: Add validation
        newAlbumResult["name"] = binding.name.text.toString()
        newAlbumResult["cover"] = binding.cover.text.toString()
        newAlbumResult["releaseDate"] = binding.releaseDate.text.toString()
        newAlbumResult["description"] = binding.description.text.toString()
        newAlbumViewModel.saveNewAlbum(newAlbumResult)
        Toast.makeText(context, "Agregado", Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
        return true
    }
}