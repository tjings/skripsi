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
                        view?.stopLoading()
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            view?.onLoginSuccesful()
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

}