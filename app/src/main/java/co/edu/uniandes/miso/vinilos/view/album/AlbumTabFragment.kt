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
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.FragmentAlbumTabBinding
import co.edu.uniandes.miso.vinilos.view.performer.PerformerDetailFragment
import co.edu.uniandes.miso.vinilos.viewmodel.album.AlbumDetailViewModel
import co.edu.uniandes.miso.vinilos.viewmodel.performer.PerformerDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumTabFragment : Fragment() {

    private var _binding: FragmentAlbumTabBinding? = null
    private val binding get() = _binding!!

    private val albumDetailViewModel: AlbumDetailViewModel by viewModels()
    private val performerDetailViewModel: PerformerDetailViewModel by viewModels()

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.albumDetailProgressBar
        progressBar.visibility = View.VISIBLE

        setupObservers()

        val albumId = arguments?.getInt("albumId") ?: -1
        loadData(albumId)

        setupViewPager(albumId)
    }

    private fun setupObservers() {
        albumDetailViewModel.album.observe(viewLifecycleOwner) { album ->
            (activity as? AppCompatActivity)?.supportActionBar?.title = album.name
        }

        albumDetailViewModel.comments.observe(viewLifecycleOwner) { _ ->
            progressBar.visibility = View.GONE
        }

        albumDetailViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupViewPager(albumId: Int) {
        val adapter = AlbumTabsAdapter(this)
        binding.albumDetailPager.adapter = adapter

        val tabTitles = listOf(
            getString(R.string.album_detail_general_tab),
            getString(R.string.performer_detail_title),
            getString(R.string.album_detail_comments_tab)
        )

        TabLayoutMediator(binding.albumDetailTabs, binding.albumDetailPager) { tab, position ->
            tab.text = tabTitles[position].uppercase()
        }.attach()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(albumId: Int) {
        albumDetailViewModel.loadAlbumDetail(albumId)
        performerDetailViewModel.loadPerformerDetailByAlbumId(albumId)
        albumDetailViewModel.loadCommentDetail(albumId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class AlbumTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AlbumDetailFragment()
                1 -> PerformerDetailFragment()
                2 -> AlbumCommentFragment()
                else -> throw IllegalArgumentException("Invalid tab position")
            }
        }
    }
}
