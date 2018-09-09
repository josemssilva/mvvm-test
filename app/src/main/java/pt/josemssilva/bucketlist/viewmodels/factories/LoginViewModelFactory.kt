package pt.josemssilva.bucketlist.viewmodels.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pt.josemssilva.bucketlist.data.managers.SessionManager
import pt.josemssilva.bucketlist.viewmodels.LoginViewModel

/**
 * Created by josesilva on 10/04/18.
 */
class LoginViewModelFactory(val sessionManager: SessionManager) : ViewModelProvider.Factory {

    // TODO find a way to inject other dependencies in VM constructors

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (modelClass) {
            LoginViewModel::class.java -> return LoginViewModel(sessionManager) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}