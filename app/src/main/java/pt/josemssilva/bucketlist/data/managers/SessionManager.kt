package pt.josemssilva.bucketlist.data.managers

import android.util.Log
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import pt.josemssilva.bucketlist.data.models.Session
import pt.josemssilva.bucketlist.data.models.User
import java.lang.Exception


interface SessionManager {
    fun getSession(): Session
    fun authenticateUser(method: AuthenticateMethod): Single<Session>
    fun logoutUser()

    class Factory {
        companion object {
            fun create(): SessionManager = SessionManagerImpl()
        }
    }
}

private class SessionManagerImpl : SessionManager {

    override fun getSession(): Session {
        val user = FirebaseAuth.getInstance().currentUser

        return if (user != null) {
            Session("", User(user.email ?: "", user.displayName ?: ""))
        } else {
            Session.NULL
        }
    }

    override fun authenticateUser(method: AuthenticateMethod): Single<Session> {
        val singleSession = SingleOnSubscribe<Session> { emitter ->
            val mAuth = FirebaseAuth.getInstance()
            val credential = when (method) {
                is AuthenticateMethod.Facebook -> FacebookAuthProvider.getCredential(method.result.accessToken.token)
                is AuthenticateMethod.Google -> GoogleAuthProvider.getCredential(method.account.idToken, null)
            }

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            if (user != null) {
                                emitter.onSuccess(getSession())
                            } else {
                                emitter.onError(Exception("error logging to Firebase"))
                            }
                        } else {
                            emitter.onError(Exception("error logging to Firebase"))
                        }
                    }
        }

        return Single.create(singleSession)
    }

    override fun logoutUser() {
//        LoginManager.getInstance().logOut()

        FirebaseAuth.getInstance().signOut()
    }
}

sealed class AuthenticateMethod {
    class Facebook(val result: LoginResult) : AuthenticateMethod()
    class Google(val account: GoogleSignInAccount) : AuthenticateMethod()
}