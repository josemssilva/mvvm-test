package pt.josemssilva.bucketlist.data.managers

import android.util.Log
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import pt.josemssilva.bucketlist.data.models.Session
import pt.josemssilva.bucketlist.data.models.User
import java.lang.Exception

interface SessionManager {
    fun getSession(): Session
    fun authenticateUser(externalToken: String): Single<Session>
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

        if (user != null) {
            return Session("", User(user.email ?: "", user.displayName ?: ""))
        } else {
            return Session.NULL
        }
    }

    override fun authenticateUser(externalToken: String): Single<Session> {

        val singleSession = SingleOnSubscribe<Session> { emitter ->
            val mAuth = FirebaseAuth.getInstance()
            val credential = FacebookAuthProvider.getCredential(externalToken)
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        Log.e("TESTE", "ON COMPLETE!!")
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
                    .addOnSuccessListener { authResult ->
                        Log.e("TESTE", "ON SUCCESS!!")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("TESTE", "ON FAILURE!!")
                        emitter.onError(exception)
                    }
                    .addOnCanceledListener {
                        Log.e("TESTE", "ON CANCELED!!")
                        emitter.onError(Exception("error logging to Firebase"))
                    }
        }

        return Single.create(singleSession)
    }

    override fun logoutUser() {
//        LoginManager.getInstance().logOut()

        FirebaseAuth.getInstance().signOut()
    }
}