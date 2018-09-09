package pt.josemssilva.bucketlist.viewmodels.actions

sealed class EditableActions {
    object DataUpdated : EditableActions()
    data class DataAdded(val id: String) : EditableActions()
}