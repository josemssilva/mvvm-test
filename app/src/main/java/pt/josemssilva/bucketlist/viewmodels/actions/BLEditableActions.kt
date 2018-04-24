package pt.josemssilva.bucketlist.viewmodels.actions

sealed class BLEditableActions {
    object DataUpdated : BLEditableActions()
    data class DataAdded(val id: String) : BLEditableActions()
}