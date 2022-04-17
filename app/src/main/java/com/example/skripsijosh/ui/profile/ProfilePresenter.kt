package com.example.skripsijosh.ui.profile

import android.util.Log
import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserData

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
}