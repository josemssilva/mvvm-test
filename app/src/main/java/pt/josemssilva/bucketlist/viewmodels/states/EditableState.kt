package pt.josemssilva.bucketlist.viewmodels.states

import pt.josemssilva.bucketlist.data.models.GroceryItem

sealed class EditableState {
    object DataProcessing : EditableState()
    data class Error(val message: String?) : EditableState()
    data class Data(val data: GroceryItem?) : EditableState()
}