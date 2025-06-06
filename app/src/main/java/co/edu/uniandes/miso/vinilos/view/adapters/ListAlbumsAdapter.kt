package co.edu.uniandes.miso.vinilos.view.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.ListAlbumItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedAlbum
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File
import java.lang.ref.WeakReference

class ListAlbumsAdapter(
    private val onAlbumClick: (Int, String, String) -> Unit
) : RecyclerView.Adapter<ListAlbumsAdapter.ListAlbumsViewHolder>() {

    private var albums: WeakReference<List<SimplifiedAlbum>> = WeakReference(emptyList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAlbumsViewHolder {
        val withDataBinding: ListAlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ListAlbumsViewHolder.LAYOUT,
            parent,
            false
        )
        return ListAlbumsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ListAlbumsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = getAlbums()[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            onAlbumClick(getAlbums()[position].id, getAlbums()[position].name, getAlbums()[position].cover)
        }
    }

    override fun getItemCount(): Int {
        return getAlbums().size
    }

    private fun getAlbums(): List<SimplifiedAlbum> {
        return albums.get()!!
    }

    fun setAlbums(newAlbums: List<SimplifiedAlbum>) {

        this.albums = WeakReference(newAlbums)
        notifyDataSetChanged()
    }

    class ListAlbumsViewHolder(val viewDataBinding: ListAlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_album_item
        }
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, path: String?) {
    if (path.isNullOrEmpty()) return

    val uri = if (path.startsWith("http")) {
        path.toUri()
    } else {
        Uri.fromFile(File(path))
    }

    Glide.with(view.context)
        .load(uri)
        .placeholder(R.drawable.loading_animation)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.ic_broken_image)
        .into(view)
}