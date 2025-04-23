package co.edu.uniandes.miso.vinilos.view.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.ListAlbumItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.Album
import com.bumptech.glide.Glide
import java.io.File
import androidx.core.net.toUri

class ListAlbumsAdapter : RecyclerView.Adapter<ListAlbumsAdapter.ListAlbumsViewHolder>() {

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
            it.album = albums[position]
        }

        // TODO: Add code to handle click events
        holder.viewDataBinding.root.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return albums.size
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
        .placeholder(R.drawable.music_note_24dp)
        .error(R.drawable.music_note_24dp)
        .into(view)
}