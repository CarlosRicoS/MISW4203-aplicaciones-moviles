package co.edu.uniandes.miso.vinilos.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.ListPerformerItemBinding
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.model.domain.SimplifiedPerformer

class ListPerformersAdapter(
    private val onPerformerClick: (Int, String, String, PerformerType) -> Unit
) : RecyclerView.Adapter<ListPerformersAdapter.ListPerformersViewHolder>() {
    var performer: List<SimplifiedPerformer> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPerformersViewHolder {
        val withDataBinding: ListPerformerItemBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ListPerformersViewHolder.LAYOUT,
            parent,
            false
        )
        return ListPerformersViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ListPerformersViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.performer = performer[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            onPerformerClick(
                performer[position].id,
                performer[position].name,
                performer[position].image,
                performer[position].performerType!!
            )
        }
    }

    override fun getItemCount(): Int {
        return performer.size
    }

    class ListPerformersViewHolder(val viewDataBinding: ListPerformerItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_performer_item
        }
    }
}

@BindingAdapter("performerTypeText")
fun TextView.setPerformerTypeText(performerType: PerformerType?) {
    text = when (performerType) {
        PerformerType.BAND -> context.getString(R.string.performer_type_band)
        PerformerType.MUSICIAN -> context.getString(R.string.performer_type_musician)
        else -> ""
    }
}
