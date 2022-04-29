package umn.ac.id.skripsijosh.ui.register.biodata

import android.util.Log
import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.pojo.UserStreak
import umn.ac.id.skripsijosh.pojo.UserWater

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