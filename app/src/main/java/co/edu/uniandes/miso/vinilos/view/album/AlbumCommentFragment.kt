package co.edu.uniandes.miso.vinilos.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.AlbumDetailCommentsBinding
import co.edu.uniandes.miso.vinilos.model.settings.VinylsDataStore
import co.edu.uniandes.miso.vinilos.view.adapters.CommentAdapter
import co.edu.uniandes.miso.vinilos.viewmodel.album.AlbumDetailViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumCommentFragment : Fragment() {

    private var _binding: AlbumDetailCommentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumDetailViewModel by viewModels({ requireParentFragment() })
    private lateinit var recyclerView: RecyclerView
    private var viewModelAdapter: CommentAdapter? = null
    private lateinit var addCommentButton: AppCompatButton
    private lateinit var ratingInput: AutoCompleteTextView
    private lateinit var contentInput: TextInputEditText
    private lateinit var newCommentForm: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlbumDetailCommentsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModelAdapter = CommentAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.commentsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        ratingInput = view.findViewById(R.id.commentRating)
        addCommentButton = view.findViewById(R.id.addCommentButton)
        contentInput = view.findViewById(R.id.contentInput)
        newCommentForm = view.findViewById(R.id.newCommentForm)

        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            viewModelAdapter?.comments = comments
        }

        val options = resources.getStringArray(R.array.album_score_dropdown_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, options)
        ratingInput.setAdapter(adapter)
        ratingInput.setOnClickListener {
            ratingInput.showDropDown()
        }

        addCommentButton.setOnClickListener {

            val rating = ratingInput.text.toString()
            val content = contentInput.text.toString()

            if(rating.isBlank() || content.isBlank()) {

                val toast = Toast.makeText(context, "Ingresa correctamente los datos.", Toast.LENGTH_SHORT)
                toast.show()
            }
            else
            {

                val currentUser = VinylsDataStore.readLongProperty(requireContext(), "APP_USER_ID")
                viewModel.addComment(content, rating.toInt(), currentUser.toInt())
                cleanCommentForm()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUser = VinylsDataStore.readLongProperty(requireContext(), "APP_USER_ID")
        val visibility = if (currentUser > 0)  View.VISIBLE else View.GONE
        newCommentForm.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cleanCommentForm() {

        ratingInput.text.clear()
        contentInput.text!!.clear()
    }
}