package co.edu.uniandes.miso.vinilos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.AlbumCarouselItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum

class AlbumCarouselAdapter(
    private val onAlbumClick: (Int, String, String) -> Unit
) : RecyclerView.Adapter<AlbumCarouselAdapter.AlbumCarouselViewHolder>() {

    private var albums: List<SimplifiedAlbum> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCarouselViewHolder {
        val withDataBinding: AlbumCarouselItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumCarouselViewHolder.LAYOUT,
            parent,
            false
        )
        return AlbumCarouselViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumCarouselViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = albums[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            val album = albums[position]
            onAlbumClick(
                album.id,
                album.name,
                album.cover
            )
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun setAlbums(newAlbums: List<SimplifiedAlbum>) {
        val diffCallback = AlbumDiffCallback(albums, newAlbums)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        
        this.albums = newAlbums
        diffResult.dispatchUpdatesTo(this)
    }

    class AlbumCarouselViewHolder(val viewDataBinding: AlbumCarouselItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_carousel_item
        }
    }
    
    private class AlbumDiffCallback(
        private val oldList: List<SimplifiedAlbum>,
        private val newList: List<SimplifiedAlbum>
    ) : DiffUtil.Callback() {
        
        override fun getOldListSize(): Int = oldList.size
        
        override fun getNewListSize(): Int = newList.size
        
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            
            return oldItem.id == newItem.id && 
                   oldItem.name == newItem.name &&
                   oldItem.cover == newItem.cover &&
                   oldItem.author == newItem.author
        }
    }
} 