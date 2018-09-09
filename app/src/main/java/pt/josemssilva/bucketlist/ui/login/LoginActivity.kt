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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.reactivex.observers.DisposableObserver
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.list.ListActivity
import pt.josemssilva.bucketlist.viewmodels.LoginViewModel
import pt.josemssilva.bucketlist.viewmodels.actions.LoginActions
import pt.josemssilva.bucketlist.viewmodels.factories.LoginViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.states.LoginState
import java.lang.Exception
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login_layout.*


private const val RC_SIGN_IN = 10982

class LoginActivity : BaseActivity() {

    private val callbackManager = CallbackManager.Factory.create()
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("439149913725-av4g2uecin3ublvesn8e6pjtvrrnpq7t.apps.googleusercontent.com")
                .requestEmail()
                .build()

        GoogleSignIn.getClient(this@LoginActivity, gso)
    }
    private val viewModel: LoginViewModel by lazy {
        val factory = LoginViewModelFactory(getApp().sessionManager)
        ViewModelProviders.of(this@LoginActivity, factory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindUiObservers()
        setupFBLogin()
        setupGoogleSignIn()
        showContentView()
    }

    override fun layoutId() = R.layout.login_layout

    override fun handleSubscriptions() {
        addSubscription(viewModel.getActionsObservable().subscribeWith(object : DisposableObserver<LoginActions>() {
            override fun onComplete() {}

            override fun onNext(t: LoginActions) {
                when (t) {
                    LoginActions.LoginCompleted -> navigateTo(ListActivity::class.java)
                }
            }

            override fun onError(e: Throwable) {}
        }))
    }

    private fun bindUiObservers() {
        viewModel.getStateObservable().observe(
                this@LoginActivity,
                Observer { state ->
                    when (state) {
                        is LoginState.LoginProcessing -> showLoadingView()
                        is LoginState.Error -> {
                            showContentView()
                            Snackbar.make(this@LoginActivity.mView, state.message
                                    ?: "something happened", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
        )
    }

    private fun setupFBLogin() {

        login_button.setReadPermissions(listOf("email", "public_profile"))
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                    viewModel.authenticateWithFacebook(result)
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
                Snackbar.make(this@LoginActivity.mView, error?.message
                        ?: "Something happened", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupGoogleSignIn() {
        sign_in_button.setOnClickListener { view ->
            val signInIntent = googleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            viewModel.authenticateWithGoogle(account)
        } catch (e: ApiException) {
            Snackbar.make(this@LoginActivity.mView, e.message
                    ?: "Something happened", Snackbar.LENGTH_SHORT).show()
        }
    }
}