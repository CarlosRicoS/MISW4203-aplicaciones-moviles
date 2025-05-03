package co.edu.uniandes.miso.vinilos.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.edu.uniandes.miso.vinilos.databinding.AlbumDetailBinding
import co.edu.uniandes.miso.vinilos.viewmodel.album.AlbumDetailViewModel
import com.bumptech.glide.Glide

class AlbumDetailFragment : Fragment() {
    private val viewModel: AlbumDetailViewModel by viewModels({ requireParentFragment() })
    private var _binding: AlbumDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = AlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.album.observe(viewLifecycleOwner) { album ->
            binding.album = album
            Glide.with(binding.root).load(album.cover).into(binding.albumCover)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}