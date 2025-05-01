package co.edu.uniandes.miso.vinilos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.ListCollectorItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector

class ListCollectorsAdapter(
    private val onCollectorClick: (Int, String) -> Unit
) : RecyclerView.Adapter<ListCollectorsAdapter.ListCollectorsViewHolder>() {
    var collector: List<SimplifiedCollector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCollectorsViewHolder {
        val withDataBinding: ListCollectorItemBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ListCollectorsViewHolder.LAYOUT,
            parent,
            false
        )
        return ListCollectorsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ListCollectorsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collector[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            onCollectorClick(collector[position].id, collector[position].name)
        }
    }

    override fun getItemCount(): Int {
        return collector.size
    }

    class ListCollectorsViewHolder(val viewDataBinding: ListCollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_collector_item
        }
    }
}