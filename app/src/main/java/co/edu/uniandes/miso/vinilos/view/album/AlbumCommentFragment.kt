package co.edu.uniandes.miso.vinilos.view.album

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.AlbumDetailCommentsBinding
import co.edu.uniandes.miso.vinilos.view.adapters.CommentAdapter
import co.edu.uniandes.miso.vinilos.viewmodel.album.AlbumDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumCommentFragment : Fragment() {

    private var _binding: AlbumDetailCommentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumDetailViewModel by viewModels({ requireParentFragment() })
    private lateinit var recyclerView: RecyclerView
    private var viewModelAdapter: CommentAdapter? = null

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

        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            viewModelAdapter?.comments = comments
        }

        val dropdown = binding.dropdown
        val options = resources.getStringArray(R.array.album_score_dropdown_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, options)
        dropdown.setAdapter(adapter)
        dropdown.setOnClickListener {
            dropdown.showDropDown()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}