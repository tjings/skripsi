package com.example.skripsijosh.ui.register.biodata

import android.util.Log
import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserData
import com.example.skripsijosh.pojo.UserWater

class BiodataPresenter (view: BiodataView) : BasePresenter <BiodataView>() {

    init {
        super.attachView(view)
    }

    fun addUserData(displayName: String,
                    bday: String,
                    weight: String,
                    height: String,
                    displayPic: String?) {
        view?.startLoading()
        val user = UserData(
            displayName = displayName,
            bday = bday,
            weight = weight,
            height = height,
            displayPic = displayPic,
            isBiodataDone = true
        )
        val water = UserWater(0)
        db.collection("userData")
            .document(auth.uid!!)
            .set(user)
            .addOnSuccessListener {
                Log.d("sukses", "Done")
                view?.stopLoading()
                view?.onSaveBiodataSucces()
            }
            .addOnFailureListener {
                Log.w("eror", "Error", it)
            }
        db.collection("userWater")
            .document(auth.uid!!)
            .set(water)
            .addOnSuccessListener {}
            .addOnFailureListener {}
    }
}