package co.edu.uniandes.miso.vinilos.view.collector

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.FragmentCollectorDetailBinding
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.view.adapters.AlbumCarouselAdapter
import co.edu.uniandes.miso.vinilos.view.adapters.PerformerCarouselAdapter
import co.edu.uniandes.miso.vinilos.viewmodel.collector.CollectorDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectorDetailFragment : Fragment() {

    private val collectorDetailViewModel: CollectorDetailViewModel by viewModels()
    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar
    private lateinit var performersRecyclerView: RecyclerView
    private lateinit var albumsRecyclerView: RecyclerView
    private lateinit var performerAdapter: PerformerCarouselAdapter
    private lateinit var albumAdapter: AlbumCarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupObservers()

        progressBar = binding.collectorDetailProgressBar
        
        val collectorId = arguments?.getInt("collectorId") ?: -1
        Log.d("CollectorDetailFragment", "Collector ID: $collectorId")
        loadData(collectorId)
    }

    private fun setupRecyclerViews() {
        performersRecyclerView = binding.performersRecyclerView
        
        val performersLayoutManager = object : LinearLayoutManager(requireContext(), HORIZONTAL, false) {
            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                super.onLayoutChildren(recycler, state)
                
                if (itemCount == 1 && childCount == 1) {
                    val view = getChildAt(0) ?: return
                    val width = width
                    val decoratedViewWidth = getDecoratedMeasuredWidth(view)
                    val leftMargin = (width - decoratedViewWidth) / 2
                    
                    view.layoutParams = (view.layoutParams as RecyclerView.LayoutParams).apply {
                        marginStart = leftMargin
                        marginEnd = leftMargin
                    }
                }
            }
        }
        
        performersRecyclerView.layoutManager = performersLayoutManager
        performerAdapter = PerformerCarouselAdapter { performerId, performerName, performerImage, performerType ->
            navigateToPerformerDetail(performerId, performerName, performerImage, performerType)
        }
        performersRecyclerView.adapter = performerAdapter

        albumsRecyclerView = binding.albumsRecyclerView
        
        val albumsLayoutManager = object : LinearLayoutManager(requireContext(), HORIZONTAL, false) {
            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                super.onLayoutChildren(recycler, state)
                
                if (itemCount == 1 && childCount == 1) {
                    val view = getChildAt(0) ?: return
                    val width = width
                    val decoratedViewWidth = getDecoratedMeasuredWidth(view)
                    val leftMargin = (width - decoratedViewWidth) / 2
                    
                    view.layoutParams = (view.layoutParams as RecyclerView.LayoutParams).apply {
                        marginStart = leftMargin
                        marginEnd = leftMargin
                    }
                }
            }
        }
        
        albumsRecyclerView.layoutManager = albumsLayoutManager
        albumAdapter = AlbumCarouselAdapter { albumId, albumName, albumCover ->
            navigateToAlbumDetail(albumId, albumName, albumCover)
        }
        albumsRecyclerView.adapter = albumAdapter
    }

    private fun navigateToPerformerDetail(performerId: Int, performerName: String, performerImage: String, performerType: PerformerType) {
        val bundle = Bundle().apply {
            putInt("performerId", performerId)
            putString("performerName", performerName)
            putString("performerImage", performerImage)
            putString("performerType", performerType.toString())
        }
        findNavController().navigate(R.id.performerDetailContainerFragment, bundle)
    }

    private fun navigateToAlbumDetail(albumId: Int, albumName: String, albumCover: String) {
        val bundle = Bundle().apply {
            putInt("albumId", albumId)
            putString("albumName", albumName)
            putString("albumCover", albumCover)
        }
        findNavController().navigate(R.id.albumDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        collectorDetailViewModel.collector.observe(viewLifecycleOwner) { collector ->
            binding.collector = collector
            (activity as? AppCompatActivity)?.supportActionBar?.title = collector.name
            
            performerAdapter.setPerformers(collector.favoritePerformers)
            albumAdapter.setAlbums(collector.collectedAlbums)
        }
        
        collectorDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        collectorDetailViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(collectorId: Int) {
        collectorDetailViewModel.loadCollectorDetail(collectorId)
    }
}

@BindingAdapter("underlinedText")
fun setUnderlinedText(view: TextView, text: String?) {
    if (text != null) {
        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.length, 0)
        view.text = spannable
    }
}
