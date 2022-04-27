package com.example.skripsijosh.ui.leaderboard

import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserStreak
import com.google.firebase.firestore.Query

class LeaderboardPresenter (view: LeaderboardView) : BasePresenter<LeaderboardView>() {

    init {
        super.attachView(view)
    }

    fun getLeaderBoard() {
        view?.startLoading()
        db.collection("userStreak")
            .orderBy("highestStreak", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener {
                view?.stopLoading()
                val results = it.toObjects(UserStreak::class.java)
                view?.onLoadDataSuccess(results)
            }
            .addOnFailureListener {
                view?.stopLoading()
                view?.showError(it.message.toString())
            }
    }

}