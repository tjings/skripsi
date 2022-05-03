package umn.ac.id.skripsijosh.ui.challenge

import umn.ac.id.skripsijosh.base.BasePresenter
import com.google.firebase.firestore.FieldValue
import umn.ac.id.skripsijosh.pojo.*

class ChallengePresenter (view: ChallengeView) : BasePresenter <ChallengeView>() {

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
            .addOnFailureListener { }
    }

    fun setMedal(medalId: Int) {
        db.collection("userCompletedMedal")
            .document(auth.uid!!)
            .update("medalId", FieldValue.arrayUnion(medalId))
    }

    fun addPoints(pointGot: Int) {
        db.collection("userBalance")
            .document(auth.uid!!)
            .update("balance", FieldValue.increment(pointGot.toLong()))
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

    fun setUserCompletedChallenge(challengeId: Int) {
        db.collection("challengeLists")
            .document(challengeId.toString())
            .update("userCompleted", FieldValue.arrayUnion(auth.uid!!))
    }
}