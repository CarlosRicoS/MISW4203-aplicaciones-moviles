package co.edu.uniandes.miso.vinilos.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.FragmentListAlbumsBinding
import co.edu.uniandes.miso.vinilos.view.adapters.ListAlbumsAdapter
import co.edu.uniandes.miso.vinilos.viewmodel.album.ListAlbumsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAlbumsFragment : Fragment() {

    private var _binding: FragmentListAlbumsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ListAlbumsViewModel by viewModels()
    private var viewModelAdapter: ListAlbumsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListAlbumsBinding.inflate(inflater, container, false)
        viewModelAdapter = ListAlbumsAdapter(){ albumId, albumName, albumCover ->

            val bundle = Bundle().apply {
                putInt("albumId", albumId)
                putString("albumName", albumName)
                putString("albumCover", albumCover)
            }
            findNavController().navigate(R.id.albumDetailFragment, bundle)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.albumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            viewModelAdapter?.setAlbums(albums)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.albumListProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.newAlbum.setOnClickListener {
            findNavController().navigate(R.id.action_listAlbum_to_newAlbumForm)
        }

        viewModel.loadAlbums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}