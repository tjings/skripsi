package umn.ac.id.skripsijosh.ui.register

import umn.ac.id.skripsijosh.base.BasePresenter

class RegisterPresenter (view: RegisterView) : BasePresenter<RegisterView>() {

    init {
        super.attachView(view)
    }

    fun registerUser(activity: RegisterActivity, email: String?, password: String?) {
        view?.startLoading()
        if(currentUser != null) {
            return
        }
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(activity) { task ->
                if(task.isSuccessful) {
                    view?.stopLoading()
                    view?.onRegisterSuccess()
                } else {
                    view?.stopLoading()
                    view?.showError(task.exception?.message.toString())
                }
            }

    }

}