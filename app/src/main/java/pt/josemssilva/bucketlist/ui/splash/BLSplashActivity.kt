package pt.josemssilva.bucketlist.ui.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import io.reactivex.observers.DisposableObserver
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.list.BLListActivity
import pt.josemssilva.bucketlist.ui.login.BLLoginActivity
import pt.josemssilva.bucketlist.viewmodels.BLSplashViewModel
import pt.josemssilva.bucketlist.viewmodels.actions.BLSplashActions
import pt.josemssilva.bucketlist.viewmodels.factories.BLSplashViewModelFactory

class BLSplashActivity : BaseActivity() {

    val viewModel: BLSplashViewModel by lazy {
        ViewModelProviders.of(this, BLSplashViewModelFactory(getApp().sessionManager)).get(BLSplashViewModel::class.java)
    }

    override fun layoutId() = R.layout.splash_layout

    override fun handleSubscriptions() {
        val observer = viewModel.actions.subscribeWith(object : DisposableObserver<BLSplashActions>() {
            override fun onComplete() {}

            override fun onNext(t: BLSplashActions) {
                when (t) {
                    BLSplashActions.GoDoLogin -> {
                        navigateTo(BLLoginActivity::class.java)
                        finish()
                    }
                    BLSplashActions.GoHome -> {
                        navigateTo(BLListActivity::class.java)
                        finish()
                    }
                }
            }

            override fun onError(e: Throwable) {
                navigateTo(BLLoginActivity::class.java)
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