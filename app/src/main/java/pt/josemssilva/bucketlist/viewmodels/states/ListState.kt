package pt.josemssilva.bucketlist.viewmodels.states

import pt.josemssilva.bucketlist.data.models.GroceryItem

sealed class ListState {
    object DataFetching : ListState()
    data class Error(val message: String?) : ListState()
    data class Data(val data: List<GroceryItem>) : ListState()
}