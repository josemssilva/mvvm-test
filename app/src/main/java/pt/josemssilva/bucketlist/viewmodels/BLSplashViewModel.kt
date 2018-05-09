package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.managers.SessionManager
import pt.josemssilva.bucketlist.data.models.Session
import pt.josemssilva.bucketlist.viewmodels.actions.BLSplashActions

class BLSplashViewModel(val sessionManager: SessionManager) : ViewModel() {
    val actions = PublishSubject.create<BLSplashActions>()

    fun requestFlowDecision() {
        actions.onNext(
                if (!sessionManager.getSession().isNull) {
                    BLSplashActions.GoHome
                } else {
                    BLSplashActions.GoDoLogin
                }
        )
    }
}