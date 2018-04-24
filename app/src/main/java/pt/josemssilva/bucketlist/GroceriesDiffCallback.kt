package pt.josemssilva.bucketlist

import android.support.v7.util.DiffUtil
import pt.josemssilva.bucketlist.data.models.GroceryItem

/**
 * Created by josesilva on 05/04/18.
 */
class GroceriesDiffCallback(val oldList: List<GroceryItem>, val newList: List<GroceryItem>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id.equals(newList[newItemPosition].id)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].equals(newList[newItemPosition])
}