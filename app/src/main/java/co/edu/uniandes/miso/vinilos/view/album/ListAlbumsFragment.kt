package co.edu.uniandes.miso.vinilos.view.album

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.databinding.FragmentListAlbumsBinding
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.*
import co.edu.uniandes.miso.vinilos.view.adapters.ListAlbumsAdapter
import co.edu.uniandes.miso.vinilos.viewmodel.album.ListAlbumsViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

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
        viewModelAdapter = ListAlbumsAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.albumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        lifecycleScope.launch {
            viewModel.loadAlbums()
            viewModelAdapter?.albums = viewModel.getAlbums()
            binding.progressBar.visibility = View.GONE
        }

    }

    //    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val activity = requireNotNull(this.activity) {
//            "You can only access the viewModel after onActivityCreated()"
//        }
//
//        //activity.actionBar?.title = getString(R.string.title_albums)
//        //viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application)).get(AlbumViewModel::class.java)
//        //viewModel.albums.observe(viewLifecycleOwner, Observer<List<AlbumDTO>> {
//        //    it.apply {
//        //        viewModelAdapter!!.albums = this
//        //    }
//        //})
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}