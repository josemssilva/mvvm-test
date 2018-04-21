package pt.josemssilva.bucketlist.viewmodels.actions

import pt.josemssilva.bucketlist.model.models.GroceryItem

sealed class BLListActions {
    object AddItem : BLListActions()
    data class ItemDetail(val itemId: String) : BLListActions()
}