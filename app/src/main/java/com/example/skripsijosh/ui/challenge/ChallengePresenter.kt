package com.example.skripsijosh.ui.challenge

import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.ChallengeDetails
import com.example.skripsijosh.pojo.UserStreak
import com.example.skripsijosh.pojo.UserWater
import com.google.firebase.firestore.auth.User

class ChallengePresenter (view:ChallengeView) : BasePresenter <ChallengeView>() {

    init {
        super.attachView(view)
    }

    fun getChallengeList() {
        db.collection("challengeLists")
            .get()
            .addOnSuccessListener {
                view?.stopLoading()
                val result = it.toObjects(ChallengeDetails::class.java)
                view?.onGetChallengeSuccess(result)
            }
            .addOnFailureListener {  }
    }

    fun getMilestones() {
        db.collection("userStreak")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener { streak ->
                val mStreak = streak.toObject(UserStreak::class.java)
                db.collection("userWater")
                    .document(auth.uid!!)
                    .get().addOnSuccessListener { water->
                        val mWater = water.toObject(UserWater::class.java)
                        view?.onGetUserMilestonesSuccess(mStreak!!, mWater!!)
                    }
            }
    }
}