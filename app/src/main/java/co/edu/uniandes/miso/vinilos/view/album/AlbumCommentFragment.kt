package co.edu.uniandes.miso.vinilos.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}