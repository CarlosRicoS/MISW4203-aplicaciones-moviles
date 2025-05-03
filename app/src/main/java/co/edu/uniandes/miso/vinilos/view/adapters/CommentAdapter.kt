package co.edu.uniandes.miso.vinilos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.AlbumDetailCommentItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.DetailComment

class CommentAdapter() :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    var comments: List<DetailComment> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val withDataBinding: AlbumDetailCommentItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), CommentViewHolder.LAYOUT, parent, false
        )
        return CommentViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.comment = comments[position]
        }
    }

    override fun getItemCount() = comments.size

    class CommentViewHolder(val viewDataBinding: AlbumDetailCommentItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_detail_comment_item
        }
    }
}