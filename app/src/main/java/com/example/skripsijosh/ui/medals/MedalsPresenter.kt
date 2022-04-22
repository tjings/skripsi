package com.example.skripsijosh.ui.medals

import android.util.Log
import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserCompMedal
import com.example.skripsijosh.pojo.UserMedal

class MedalsPresenter (view: MedalsView) : BasePresenter <MedalsView>() {

    init {
        super.attachView(view)
    }

    fun getUserMedals() {
        view?.startLoading()
        db.collection("userCompletedMedal")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener { data ->
                val medalId = data.get("medalId") as List<*>?
                if (medalId != null) {
                    getUserMedalsDetails(medalId)
                } else {
                    val userCompMedal = UserCompMedal(medalId = arrayListOf(1))
                    db.collection("userCompletedMedal")
                        .document(auth.uid!!)
                        .set(userCompMedal)
                        .addOnSuccessListener {
                            getUserMedals()
                        }
                }
            }
            .addOnFailureListener {  }
    }

    private fun getUserMedalsDetails(medalId: List<*>) {
        medalId.forEach { any ->
            db.collection("userMedal")
                .document(any.toString())
                .get()
                .addOnSuccessListener {
                    view?.stopLoading()
                    val result = it.toObject(UserMedal::class.java)
                    view?.onGetMedalSuccess(result!!)
                }
                .addOnFailureListener {  }
        }
    }

}