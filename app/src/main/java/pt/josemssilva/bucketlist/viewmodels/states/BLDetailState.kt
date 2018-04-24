package pt.josemssilva.bucketlist.viewmodels.states

import pt.josemssilva.bucketlist.data.models.GroceryItem

sealed class BLDetailState {
    object DataFetch : BLDetailState()
    data class Error(val message: String?) : BLDetailState()
    data class Data(val data: GroceryItem) : BLDetailState()
}