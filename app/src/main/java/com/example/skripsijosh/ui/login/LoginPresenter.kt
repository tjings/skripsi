package com.example.skripsijosh.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.skripsijosh.base.BasePresenter

class LoginPresenter (view: LoginView) : BasePresenter<LoginView>() {

    init {
        super.attachView(view)
    }

    fun verifyUser(activity: LoginActivity, email: String?, password: String?) {
        view?.startLoading()
        if (email != null) {
            if (password != null) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            view?.stopLoading()
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
//                            val user = auth.currentUser
//                            updateUI(user)
                            view?.onLoginSuccesful()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
//                            updateUI(null)
                        }
                    }
            }
        }

    }

}