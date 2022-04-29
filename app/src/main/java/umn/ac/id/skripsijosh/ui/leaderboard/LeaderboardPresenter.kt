package umn.ac.id.skripsijosh.ui.leaderboard

import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.UserStreak
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