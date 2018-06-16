package pt.josemssilva.bucketlist.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import io.reactivex.observers.DisposableObserver
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.list.BLListActivity
import pt.josemssilva.bucketlist.viewmodels.BLLoginViewModel
import pt.josemssilva.bucketlist.viewmodels.actions.BLLoginActions
import pt.josemssilva.bucketlist.viewmodels.factories.BLLoginViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.states.BLLoginState
import java.lang.Exception

class BLLoginActivity : BaseActivity() {

    private val callbackManager = CallbackManager.Factory.create()
    private val viewModel: BLLoginViewModel by lazy {
        val factory = BLLoginViewModelFactory(getApp().sessionManager)
        ViewModelProviders.of(this@BLLoginActivity, factory).get(BLLoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindUiObservers()
        setupFBLogin()
        showContentView()
    }

    override fun layoutId() = R.layout.login_layout

    override fun handleSubscriptions() {
        addSubscription(viewModel.getActionsObservable().subscribeWith(object : DisposableObserver<BLLoginActions>() {
            override fun onComplete() {}

            override fun onNext(t: BLLoginActions) {
                when (t) {
                    BLLoginActions.LoginCompleted -> navigateTo(BLListActivity::class.java)
                }
            }

            override fun onError(e: Throwable) {}
        }))
    }

    fun bindUiObservers() {
        viewModel.getStateObservable().observe(
                this@BLLoginActivity,
                Observer { state ->
                    when (state) {
                        is BLLoginState.LoginProcessing -> showLoadingView()
                        is BLLoginState.Error -> {
                            showContentView()
                            Snackbar.make(this@BLLoginActivity.mView, state.message
                                    ?: "something happened", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
        )
    }

    fun setupFBLogin() {
        val loginManager = LoginManager.getInstance()

        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                    viewModel.authenticateUser(result.accessToken.token)

//                    val mAuth = FirebaseAuth.getInstance()
//                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
//                    mAuth.signInWithCredential(credential)
//                            .addOnCompleteListener { task ->
//                                Log.e("TESTE", "ON COMPLETE!!")
//                                if (task.isSuccessful) {
//                                    val user = mAuth.currentUser
//
//                                } else {
//
//                                }
//                            }
//                            .addOnSuccessListener { authResult ->
//                                Log.e("TESTE", "ON SUCCESS!!")
//                            }
//                            .addOnFailureListener { exception ->
//                                Log.e("TESTE", "ON FAILURE!!")
//
//                            }
//                            .addOnCanceledListener {
//                                Log.e("TESTE", "ON CANCELED!!")
//
//                            }


                } else {
                    showSnackBar(Exception("Login failed"))
                }
            }

            override fun onCancel() {
                showSnackBar(Exception("Login Canceled"))
            }

            override fun onError(error: FacebookException?) {
                showSnackBar(error)
            }

            private fun showSnackBar(error: Exception?) {
                Snackbar.make(this@BLLoginActivity.mView, error?.message
                        ?: "Something happened", Snackbar.LENGTH_SHORT).show()
            }
        })

        loginManager.logInWithReadPermissions(this@BLLoginActivity, listOf("email", "public_profile"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}