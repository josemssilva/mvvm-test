package pt.josemssilva.bucketlist.viewmodels.actions

import pt.josemssilva.bucketlist.model.models.GroceryItem

sealed class BLEditableActions {
    object DataUpdated : BLEditableActions()
    data class DataAdded(val id: String) : BLEditableActions()
}