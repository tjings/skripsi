package umn.ac.id.skripsijosh.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.UserDailyWater
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.pojo.UserStreak
import umn.ac.id.skripsijosh.pojo.UserWater


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
                    view?.stopLoading()
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
                    results.add(result)
                }
                db.collection("userStreak")
                    .document(auth.uid!!)
                    .get()
                    .addOnSuccessListener {
                        if(it.data != null) {
                            view?.stopLoading()
                            val streak = it.toObject(UserStreak::class.java)
                            view?.onLoadDataSuccess(results, streak!!)
                        } else {
                            val initStreak = UserStreak()
                            db.collection("userStreak")
                                .document(auth.uid!!)
                                .set(initStreak)
                                .addOnSuccessListener {
                                    view?.stopLoading()
                                }
                                .addOnFailureListener {
                                    view?.showError(it.message.toString())
                                }
                        }
                    }
                    .addOnFailureListener {
                        view?.stopLoading()
                        view?.showError(it.message.toString())
                    }
            }
            .addOnFailureListener {
                view?.stopLoading()
                view?.showError(it.message.toString())
            }
    }

    fun addWater(today: String, time: String, waterAmt: Int) {
        val updatedWater = UserDailyWater(
            dailyWater = waterAmt,
            date = today
        )
        db.collection("userWater")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                val result = it.toObject(UserWater::class.java)
                val progressTotal = UserWater(result!!.progressTotal + waterAmt)
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
