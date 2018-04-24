package pt.josemssilva.bucketlist.viewmodels.actions

sealed class BLDetailActions {
    object ItemDeleted : BLDetailActions()
    data class EditItem(val itemId: String): BLDetailActions()
}