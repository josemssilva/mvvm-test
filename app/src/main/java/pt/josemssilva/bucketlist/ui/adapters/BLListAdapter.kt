package pt.josemssilva.bucketlist.ui.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.groceries_list_item.view.*
import pt.josemssilva.bucketlist.GroceriesDiffCallback
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.data.models.GroceryItem

/**
 * Created by josesilva on 05/04/18.
 */
class BLListAdapter(val itemListener: ItemListener) : RecyclerView.Adapter<BLListAdapter.GroceriesVH>() {

    interface ItemListener {
        fun onClick(item: GroceryItem)
    }

    val mItems = ArrayList<GroceryItem>()

    fun updateItems(items: List<GroceryItem>) {

        val diffResult = DiffUtil.calculateDiff(GroceriesDiffCallback(mItems, items))
        mItems.clear()
        mItems.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: GroceriesVH, position: Int) {
        holder.onBind(mItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceriesVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.groceries_list_item, parent, false)
        return GroceriesVH(view)
    }

    override fun getItemCount(): Int {
        return mItems.count()
    }

    inner class GroceriesVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun onBind(item: GroceryItem) {
            itemView.description.text = item.description
            itemView.quantity.text = item.quantity
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            itemListener.onClick(mItems[adapterPosition])
        }
    }

}