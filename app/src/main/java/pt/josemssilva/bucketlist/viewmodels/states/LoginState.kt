package pt.josemssilva.bucketlist.viewmodels.states

sealed class LoginState {
    object LoginProcessing : LoginState()
    data class Error(val message: String?) : LoginState()
}