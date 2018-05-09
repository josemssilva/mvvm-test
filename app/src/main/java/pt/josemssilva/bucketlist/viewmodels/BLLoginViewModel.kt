package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.managers.SessionManager
import pt.josemssilva.bucketlist.viewmodels.actions.BLLoginActions
import pt.josemssilva.bucketlist.viewmodels.states.BLLoginState

/**
 * Created by josesilva on 10/04/18.
 */
class BLLoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val stateObservable = MutableLiveData<BLLoginState>()
    private val actionsObservable: PublishSubject<BLLoginActions> = PublishSubject.create()

    fun getStateObservable() = stateObservable
    fun getActionsObservable() = actionsObservable


    fun authenticateUser(externalToken: String) {
        stateObservable.postValue(BLLoginState.LoginProcessing)
        sessionManager.authenticateUser(externalToken)
                .subscribeOn(Schedulers.io())
                .subscribe { session, t ->
                    if (t != null) {
                        stateObservable.postValue(BLLoginState.Error(t.message
                                ?: "Something happened while performing login"))
                    } else if (session != null) {
                        actionsObservable.onNext(BLLoginActions.LoginCompleted)
                    } else {
                        stateObservable.postValue(BLLoginState.Error("Something happened while performing login"))
                    }
                }
    }
}