package co.edu.uniandes.miso.vinilos.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.view.DrawerItem

class DrawerAdapter(
    private val items: List<DrawerItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MENU = 0
        private const val TYPE_DIVIDER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DrawerItem.MenuItem -> TYPE_MENU
            is DrawerItem.Divider -> TYPE_DIVIDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MENU -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_drawer_menu, parent, false)
                MenuViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_drawer_divider, parent, false)
                DividerViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DrawerItem.MenuItem -> (holder as MenuViewHolder).bind(item)
            is DrawerItem.Divider -> Unit
        }
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: DrawerItem.MenuItem) {
            itemView.findViewById<ImageView>(R.id.icon).setImageResource(item.iconRes)
            itemView.findViewById<TextView>(R.id.title).text = item.title
            itemView.setOnClickListener {
                onItemClick(item.id)
            }
        }
    }

    inner class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}