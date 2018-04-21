package pt.josemssilva.bucketlist.viewmodels.states

import pt.josemssilva.bucketlist.model.models.GroceryItem

sealed class BLEditableState {
    object DataProcessing : BLEditableState()
    data class Error(val message: String?) : BLEditableState()
    data class Data(val data: GroceryItem?) : BLEditableState()
}