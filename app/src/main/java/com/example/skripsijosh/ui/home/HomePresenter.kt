package com.example.skripsijosh.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserDailyWater
import com.example.skripsijosh.pojo.UserData
import com.example.skripsijosh.pojo.UserStreak
import com.example.skripsijosh.pojo.UserWater
import com.google.firebase.firestore.auth.User


class HomePresenter (view: HomeView) : BasePresenter <HomeView>() {
    init {
        super.attachView(view)
    }

    fun loadUserData() {
        db.collection("userData")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val result: UserData = it.toObject(UserData::class.java)!!
                    view?.onSuccessLoadProfile(result)
                }
            }
            .addOnFailureListener {
                view?.showError("err")
            }
    }

    fun getWaterData(today: String) {
        view?.startLoading()
        db.collection("userDailyWater")
            .document(today)
            .collection(auth.uid!!)
            .get()
            .addOnSuccessListener { data ->
                val results: MutableList<UserDailyWater> = arrayListOf()
                for (document in data) {
                    val result = document.toObject(UserDailyWater::class.java)
                    Log.d(TAG, result.toString())
                    results.add(result)
                }
                db.collection("userStreak")
                    .document(auth.uid!!)
                    .get()
                    .addOnSuccessListener {
                        view?.stopLoading()
                        val streak = it.toObject(UserStreak::class.java)
                        view?.onLoadDataSuccess(results, streak!!)
                    }
                    .addOnFailureListener {
                        view?.stopLoading()
                        view?.showError(it.message.toString())
                    }
            }
            .addOnFailureListener {
                //init empty data first
                val waterDailyDetNew = UserDailyWater(dailyWater = 0)
                db.collection("userDailyWater")
                    .document(today)
                    .collection(auth.uid!!)
                    .document("000000")
                    .set(waterDailyDetNew)
            }
    }

    fun addWater(today: String, time: String, waterAmt: Int) {
        val updatedWater = UserDailyWater(
            dailyWater = waterAmt,
            date = today
        )
        var progressTotal = UserWater()
        db.collection("userWater")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                val result = it.toObject(UserWater::class.java)
                progressTotal = UserWater(result!!.progressTotal + waterAmt)
                db.collection("userWater")
                    .document(auth.uid!!)
                    .set(progressTotal)
                    .addOnSuccessListener {
                        view?.onAddWaterSuccess()
                    }
                    .addOnFailureListener {
                        view?.showError(it.message.toString())
                    }
            }
        db.collection("userDailyWater")
            .document(today)
            .collection(auth.uid!!)
            .document(time)
            .set(updatedWater)
    }

    fun setStreak(streak: UserStreak) {
        db.collection("userStreak")
            .document(auth.uid!!)
            .set(streak)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                view?.showError(it.message.toString())
            }
    }
}
