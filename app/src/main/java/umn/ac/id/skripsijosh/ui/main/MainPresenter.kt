package umn.ac.id.skripsijosh.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.UserData

class MainPresenter (view: MainView) : BasePresenter<MainView>() {

    init {
        super.attachView(view)
    }

    fun checkBiodataDone() {
        view?.startLoading()
        db.collection("userData")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                view?.stopLoading()
                val userData = it.toObject(UserData::class.java)
                view?.onGetDataUserSucces(userData!!)
            }
    }
}