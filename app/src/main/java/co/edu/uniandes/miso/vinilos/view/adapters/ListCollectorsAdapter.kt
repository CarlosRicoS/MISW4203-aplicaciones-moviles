package co.edu.uniandes.miso.vinilos.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.ListCollectorItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedCollector
import java.lang.ref.WeakReference

class ListCollectorsAdapter(
    private val onCollectorClick: (SimplifiedCollector) -> Unit
) : RecyclerView.Adapter<ListCollectorsAdapter.ListCollectorsViewHolder>() {

    private var collectors: WeakReference<List<SimplifiedCollector>> = WeakReference(emptyList())

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
            it.collector = getCollectors()[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            onCollectorClick(getCollectors()[position])
        }
    }

    override fun getItemCount(): Int {
        return getCollectors().size
    }

    private fun getCollectors(): List<SimplifiedCollector> {
        return collectors.get()!!
    }

    fun setCollectors(newCollectors: List<SimplifiedCollector>) {

        this.collectors = WeakReference(newCollectors)
        notifyDataSetChanged()
    }

    class ListCollectorsViewHolder(val viewDataBinding: ListCollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_collector_item
        }
    }
}