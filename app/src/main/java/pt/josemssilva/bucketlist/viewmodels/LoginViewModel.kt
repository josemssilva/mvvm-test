package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.managers.AuthenticateMethod
import pt.josemssilva.bucketlist.data.managers.SessionManager
import pt.josemssilva.bucketlist.viewmodels.actions.LoginActions
import pt.josemssilva.bucketlist.viewmodels.states.LoginState

/**
 * Created by josesilva on 10/04/18.
 */
class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val stateObservable = MutableLiveData<LoginState>()
    private val actionsObservable: PublishSubject<LoginActions> = PublishSubject.create()

    fun getStateObservable() = stateObservable
    fun getActionsObservable() = actionsObservable


    private fun authenticateUser(method: AuthenticateMethod) {
        stateObservable.postValue(LoginState.LoginProcessing)

        sessionManager.authenticateUser(method)
                .subscribeOn(Schedulers.io())
                .subscribe { session, t ->
                    if (t != null) {
                        stateObservable.postValue(LoginState.Error(t.message
                                ?: "Something happened while performing login"))
                    } else if (session != null) {
                        actionsObservable.onNext(LoginActions.LoginCompleted)
                    } else {
                        stateObservable.postValue(LoginState.Error("Something happened while performing login"))
                    }
                }
    }

    fun authenticateWithFacebook(result: LoginResult) {
        authenticateUser(AuthenticateMethod.Facebook(result))
    }

    fun authenticateWithGoogle(account: GoogleSignInAccount) {
        authenticateUser(AuthenticateMethod.Google(account))
    }
}