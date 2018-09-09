package pt.josemssilva.bucketlist.viewmodels.actions

sealed class DetailActions {
    object ItemDeleted : DetailActions()
    data class EditItem(val itemId: String): DetailActions()
}