package pt.josemssilva.bucketlist.viewmodels.states

import pt.josemssilva.bucketlist.data.models.GroceryItem

sealed class BLListState {
    object DataFetching : BLListState()
    data class Error(val message: String?) : BLListState()
    data class Data(val data: List<GroceryItem>) : BLListState()
}