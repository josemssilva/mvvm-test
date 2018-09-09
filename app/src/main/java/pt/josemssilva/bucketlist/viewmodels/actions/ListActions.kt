package pt.josemssilva.bucketlist.viewmodels.actions

sealed class ListActions {
    object AddItem : ListActions()
    data class ItemDetail(val itemId: String) : ListActions()
}