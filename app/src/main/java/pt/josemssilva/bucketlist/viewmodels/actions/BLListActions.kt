package pt.josemssilva.bucketlist.viewmodels.actions

sealed class BLListActions {
    object AddItem : BLListActions()
    data class ItemDetail(val itemId: String) : BLListActions()
}