package umn.ac.id.skripsijosh.ui.register.biodata

import android.util.Log
import com.google.firebase.firestore.FieldValue
import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.*

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
            level = 0,
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
            .addOnSuccessListener {}
            .addOnFailureListener {
                Log.w("eror", "Error", it)
            }
        val streak = UserStreak()
        db.collection("userStreak")
            .document(auth.uid!!)
            .set(streak)
            .addOnSuccessListener {}
            .addOnFailureListener {}
        val userBalance = UserBalance(balance = 0)
        val inventory = UserInventory(itemHave = arrayListOf("circle"))
        db.collection("userBalance")
            .document(auth.uid!!)
            .set(userBalance)
            .addOnSuccessListener {
                db.collection("userInventory")
                    .document(auth.uid!!)
                    .set(inventory)
            }
        view?.stopLoading()
        view?.onSaveBiodataSucces()

    }
}