package pt.josemssilva.bucketlist.ui.splash

import android.arch.lifecycle.ViewModelProviders
import io.reactivex.observers.DisposableObserver
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.list.ListActivity
import pt.josemssilva.bucketlist.ui.login.LoginActivity
import pt.josemssilva.bucketlist.viewmodels.SplashViewModel
import pt.josemssilva.bucketlist.viewmodels.actions.SplashActions
import pt.josemssilva.bucketlist.viewmodels.factories.SplashViewModelFactory

class SplashActivity : BaseActivity() {

    val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this, SplashViewModelFactory(getApp().sessionManager)).get(SplashViewModel::class.java)
    }

    override fun layoutId() = R.layout.splash_layout

    override fun handleSubscriptions() {
        val observer = viewModel.actions.subscribeWith(object : DisposableObserver<SplashActions>() {
            override fun onComplete() {}

            override fun onNext(t: SplashActions) {
                when (t) {
                    SplashActions.GoDoLogin -> {
                        navigateTo(LoginActivity::class.java)
                        finish()
                    }
                    SplashActions.GoHome -> {
                        navigateTo(ListActivity::class.java)
                        finish()
                    }
                }
            }

            override fun onError(e: Throwable) {
                navigateTo(LoginActivity::class.java)
                finish()
            }
        })

        addSubscription(observer)
    }

    override fun onResume() {
        super.onResume()

        showContentView()
        viewModel.requestFlowDecision()
    }
}