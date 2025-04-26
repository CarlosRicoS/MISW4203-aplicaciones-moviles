package co.edu.uniandes.miso.vinilos.view.album

import android.content.Context
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import co.edu.uniandes.miso.vinilos.viewmodel.album.AlbumDetailViewModel
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.model.domain.DetailAlbum
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AlbumDetailFragment : Fragment() {

    companion object {
        fun newInstance() = AlbumDetailFragment()
    }

    private val viewModel: AlbumDetailViewModel by viewModels()
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var album: DetailAlbum
    private lateinit var performers: List<SimplifiedPerformer>
    private lateinit var comments: List<DetailComment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_album_detail, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tabLayout = view.findViewById(R.id.albumDetailTabs)
        viewPager = view.findViewById(R.id.albumDetailPager)
        
        observeData()
        val albumId = arguments?.getInt("albumId") ?: -1
        loadData(albumId)
    }

    private fun observeData() {
        viewModel.album.observe(viewLifecycleOwner) { album ->
            this.album = album
            (activity as? AppCompatActivity)?.supportActionBar?.title = album.name
            setupViewPager()
        }
        viewModel.performers.observe(viewLifecycleOwner) { performers ->
            this.performers = performers
        }
        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            this.comments = comments
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun setupViewPager() {
        val tabTitles = listOf(
            getString(R.string.album_detail_general_tab).uppercase(),
            getString(R.string.album_detail_performers_tab).uppercase(),
            getString(R.string.album_detail_comments_tab).uppercase()
        )
        
        val albumId = arguments?.getInt("albumId") ?: -1
        viewPager.adapter = AlbumDetailPagerAdapter(requireContext(), albumId)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData(albumId: Int) {
        viewModel.loadAlbumDetail(albumId)
        viewModel.loadPerformerDetail(albumId)
        viewModel.loadCommentDetail(albumId)
    }

    inner class AlbumDetailPagerAdapter(private val context: Context, var albumId: Int) :
        RecyclerView.Adapter<PagerViewHolder>() {

        override fun getItemCount() = 3

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
            val layoutId = when (viewType) {
                0 -> R.layout.album_detail_general
                1 -> R.layout.album_detail_performers
                2 -> R.layout.album_detail_comments
                else -> throw IllegalArgumentException()
            }

            val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return PagerViewHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            when (position) {
                0 -> fillGeneralTabData(holder)
                1 -> fillPerformersTabData(holder)
                2 -> fillCommentsTabData(holder)
                else -> throw IllegalArgumentException()
            }
        }
        
        override fun getItemViewType(position: Int) = position
        
        private fun fillGeneralTabData(holder: PagerViewHolder) {
            if (!::album.isInitialized) {
                return
            }
            
            holder.itemView.findViewById<TextView>(R.id.año).text = album.releaseDate
            holder.itemView.findViewById<TextView>(R.id.titulo).text = album.name
            holder.itemView.findViewById<TextView>(R.id.descripcion).text = album.description
            holder.itemView.findViewById<TextView>(R.id.genero).text = album.genre
            holder.itemView.findViewById<TextView>(R.id.recordLabel).text = album.recordLabel
            Glide.with(holder.itemView)
                .load(album.cover)
                .into(holder.itemView.findViewById(R.id.album_cover))
        }

        private fun fillPerformersTabData(holder: PagerViewHolder) {
            if (!::performers.isInitialized || performers.isEmpty()) {
                return
            }
            
            val performer = getPerformerAtIndex(0)
            Glide.with(holder.itemView)
                .load(performer.image)
                .into(holder.itemView.findViewById(R.id.performer_image))
            holder.itemView.findViewById<TextView>(R.id.performer_name).text = performer.name
            holder.itemView.findViewById<TextView>(R.id.description).text = performer.description
        }

        private fun getPerformerAtIndex(perfomerIndex: Int): SimplifiedPerformer {
            return performers[perfomerIndex]
        }

        private fun fillCommentsTabData(holder: PagerViewHolder) {
            if (!::comments.isInitialized) {
                return
            }
            
            val recyclerView = holder.itemView.findViewById<RecyclerView>(R.id.commentsRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = CommentAdapter(comments)
        }
    }

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class CommentAdapter(private val comments: List<DetailComment>) :
        RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.album_detail_comment_item, parent, false)
            return CommentViewHolder(view)
        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
            val comment = comments[position]
            holder.rating.text = comment.rating.toString()
            holder.description.text = comment.description
            holder.collectorName.text = comment.collector
        }

        override fun getItemCount(): Int = comments.size

        inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val rating: TextView = itemView.findViewById(R.id.comment_rating)
            val description: TextView = itemView.findViewById(R.id.comment_description)
            val collectorName: TextView = itemView.findViewById(R.id.collector_name)
        }
    }

}