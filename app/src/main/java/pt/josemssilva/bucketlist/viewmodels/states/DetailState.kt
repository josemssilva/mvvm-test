package pt.josemssilva.bucketlist.viewmodels.states

import pt.josemssilva.bucketlist.data.models.GroceryItem

sealed class DetailState {
    object DataFetch : DetailState()
    data class Error(val message: String?) : DetailState()
    data class Data(val data: GroceryItem) : DetailState()
}