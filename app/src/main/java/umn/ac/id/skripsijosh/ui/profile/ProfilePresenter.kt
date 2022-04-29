package umn.ac.id.skripsijosh.ui.profile

import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.UserData

class ProfilePresenter (view: ProfileView) : BasePresenter<ProfileView>() {

    init {
        super.attachView(view)
    }

    fun loadUserData() {
        view?.startLoading()
        db.collection("userData")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
               if(it.exists()) {
                   view?.stopLoading()
                   val result: UserData = it.toObject(UserData::class.java)!!
                   view?.onSuccessLoadProfile(result)
               }
            }
            .addOnFailureListener {
                view?.showError("err")
            }
    }

    fun saveUserData(newUserData: UserData) {
        view?.startLoading()
        db.collection("userData")
            .document(auth.uid!!)
            .set(newUserData)
            .addOnSuccessListener {
                view?.stopLoading()
                loadUserData()
            }
            .addOnFailureListener {
                view?.showError(it.toString())
            }
    }
}