package co.edu.uniandes.miso.vinilos.view.album

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.databinding.FragmentListAlbumsBinding
import co.edu.uniandes.miso.vinilos.model.data.rest.dto.album.*
import co.edu.uniandes.miso.vinilos.view.adapters.ListAlbumsAdapter

class ListAlbumsFragment : Fragment() {

    private var _binding: FragmentListAlbumsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    //private lateinit var viewModel: AlbumViewModel
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

        // ✅ Create dummy data for testing
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelAdapter?.albums = listOf(
                AlbumDTO(
                    id = 1,
                    name = "Nevermind",
                    description = "Nirvana's legendary grunge album",
                    genre = "Grunge",
                    recordLabel = "DGC",
                    releaseDate = "1991-09-24",
                    cover = "https://upload.wikimedia.org/wikipedia/en/b/b7/NirvanaNevermindalbumcover.jpg",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 2,
                    name = "Abbey Road",
                    description = "The Beatles’ classic",
                    genre = "Rock",
                    recordLabel = "Apple",
                    releaseDate = "1969-09-26",
                    cover = "https://upload.wikimedia.org/wikipedia/en/4/42/Beatles_-_Abbey_Road.jpg",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 3,
                    name = "In Utero",
                    description = "Nirvana’s intense follow-up",
                    genre = "Grunge",
                    recordLabel = "DGC",
                    releaseDate = "1993-09-21",
                    cover = "https://upload.wikimedia.org/wikipedia/en/e/e5/In_Utero_%28Nirvana%29_album_cover.jpg",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 4,
                    name = "The White Album",
                    description = "The Beatles' eclectic double LP",
                    genre = "Rock",
                    recordLabel = "Apple",
                    releaseDate = "1968-11-22",
                    cover = "https://upload.wikimedia.org/wikipedia/commons/2/20/TheBeatles68LP.jpg",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 5,
                    name = "Bleach",
                    description = "Nirvana’s raw debut album",
                    genre = "Grunge",
                    recordLabel = "Sub Pop",
                    releaseDate = "1989-06-15",
                    cover = "https://upload.wikimedia.org/wikipedia/en/a/a1/Nirvana-Bleach.jpg",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 6,
                    name = "Rubber Soul",
                    description = "The Beatles begin to evolve",
                    genre = "Rock",
                    recordLabel = "Parlophone",
                    releaseDate = "1965-12-03",
                    cover = "https://upload.wikimedia.org/wikipedia/en/5/5b/Rubber_Soul.png",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 7,
                    name = "MTV Unplugged in New York",
                    description = "Nirvana’s iconic live performance",
                    genre = "Acoustic",
                    recordLabel = "DGC",
                    releaseDate = "1994-11-01",
                    cover = "https://upload.wikimedia.org/wikipedia/en/5/54/Nirvana_mtv_unplugged_in_new_york.png",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 8,
                    name = "Sgt. Pepper’s Lonely Hearts Club Band",
                    description = "A psychedelic landmark by The Beatles",
                    genre = "Psychedelic Rock",
                    recordLabel = "Parlophone",
                    releaseDate = "1967-05-26",
                    cover = "https://upload.wikimedia.org/wikipedia/en/5/50/Sgt._Pepper%27s_Lonely_Hearts_Club_Band.jpg",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 9,
                    name = "Incesticide",
                    description = "Nirvana’s compilation of rarities",
                    genre = "Grunge",
                    recordLabel = "DGC",
                    releaseDate = "1992-12-14",
                    cover = "https://udiscovermusic.co/cdn/shop/products/720642450420_-_CD_-_NIRVANA_-_INCESTICIDE_A.png",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                ),
                AlbumDTO(
                    id = 10,
                    name = "Let It Be",
                    description = "The Beatles' final studio album",
                    genre = "Rock",
                    recordLabel = "Apple",
                    releaseDate = "1970-05-08",
                    cover = "https://upload.wikimedia.org/wikipedia/en/7/7a/The_Beatles_-_Let_It_Be.png",
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                )
            )

            binding.progressBar.visibility = View.GONE
        }, 500)
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