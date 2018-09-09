package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.managers.SessionManager
import pt.josemssilva.bucketlist.viewmodels.actions.SplashActions

class SplashViewModel(val sessionManager: SessionManager) : ViewModel() {
    val actions = PublishSubject.create<SplashActions>()

    fun requestFlowDecision() {
        actions.onNext(
                if (!sessionManager.getSession().isNull) {
                    SplashActions.GoHome
                } else {
                    SplashActions.GoDoLogin
                }
        )
    }
}