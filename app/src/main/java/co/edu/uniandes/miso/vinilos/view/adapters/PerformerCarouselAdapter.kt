package co.edu.uniandes.miso.vinilos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.PerformerCarouselItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer

class PerformerCarouselAdapter(
    private val onPerformerClick: (Int, String, String, PerformerType) -> Unit
) : RecyclerView.Adapter<PerformerCarouselAdapter.PerformerCarouselViewHolder>() {

    private var performers: List<SimplifiedPerformer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerCarouselViewHolder {
        val withDataBinding: PerformerCarouselItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            PerformerCarouselViewHolder.LAYOUT,
            parent,
            false
        )
        return PerformerCarouselViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: PerformerCarouselViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.performer = performers[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            val performer = performers[position]
            onPerformerClick(
                performer.id,
                performer.name,
                performer.image,
                performer.performerType ?: PerformerType.MUSICIAN
            )
        }
    }

    override fun getItemCount(): Int {
        return performers.size
    }

    fun setPerformers(newPerformers: List<SimplifiedPerformer>) {
        val diffCallback = PerformerDiffCallback(performers, newPerformers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        
        this.performers = newPerformers
        diffResult.dispatchUpdatesTo(this)
    }

    class PerformerCarouselViewHolder(val viewDataBinding: PerformerCarouselItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.performer_carousel_item
        }
    }
    
    private class PerformerDiffCallback(
        private val oldList: List<SimplifiedPerformer>,
        private val newList: List<SimplifiedPerformer>
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
                   oldItem.image == newItem.image &&
                   oldItem.performerType == newItem.performerType
        }
    }
} 