package pt.josemssilva.bucketlist.viewmodels.states

sealed class BLLoginState {
    object LoginProcessing : BLLoginState()
    data class Error(val message: String?) : BLLoginState()
}