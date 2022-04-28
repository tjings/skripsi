package com.example.skripsijosh.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserData

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
                if (it.data != null) {
                    Log.d(TAG, it.toString())
                    val userData = it.toObject(UserData::class.java)
                    view?.onGetDataUserSucces(userData!!)
                } else {
                    val userData = UserData()
                    db.collection("userData")
                        .document(auth.uid!!)
                        .set(userData)
                        .addOnSuccessListener {
                            checkBiodataDone()
                        }
                }
            }
    }
}