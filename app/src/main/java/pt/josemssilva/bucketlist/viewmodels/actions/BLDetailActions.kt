package pt.josemssilva.bucketlist.viewmodels.actions

import pt.josemssilva.bucketlist.model.models.GroceryItem

sealed class BLDetailActions {
    object ItemDeleted : BLDetailActions()
    data class EditItem(val itemId: String): BLDetailActions()
}