package com.example.skripsijosh.ui.register.biodata

import android.util.Log
import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserData
import com.example.skripsijosh.pojo.UserStreak
import com.example.skripsijosh.pojo.UserWater

class BiodataPresenter (view: BiodataView) : BasePresenter <BiodataView>() {

    init {
        super.attachView(view)
    }

    fun addUserData(displayName: String,
                    gender: String,
                    bday: String,
                    weight: String,
                    height: String,
                    displayPic: String?) {
        view?.startLoading()
        val user = UserData(
            displayName = displayName,
            gender = gender,
            bday = bday,
            weight = weight,
            height = height,
            displayPic = displayPic,
            isBiodataDone = true
        )
        db.collection("userData")
            .document(auth.uid!!)
            .set(user)
            .addOnSuccessListener {
                view?.stopLoading()
                view?.onSaveBiodataSucces()
            }
            .addOnFailureListener {
                Log.w("eror", "Error", it)
            }
        val water = UserWater()
        db.collection("userWater")
            .document(auth.uid!!)
            .set(water)
            .addOnSuccessListener {}
            .addOnFailureListener {}
        val streak = UserStreak(userName = displayName)
        db.collection("userStreak")
            .document(auth.uid!!)
            .set(streak)
            .addOnSuccessListener {
                view?.stopLoading()
            }
            .addOnFailureListener {
                view?.stopLoading()
                view?.showError(it.message.toString())
            }

    }
}