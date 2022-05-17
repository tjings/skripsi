package umn.ac.id.skripsijosh.ui.home

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.UserDailyWater
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.pojo.UserStreak


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

    fun checkExp(level: Int): Int {
        var result = 0
        db.collection("experienceNeeded")
            .document(level.toString())
            .get()
            .addOnSuccessListener {
                result = it.getField<Int>("exp")!!
            }
        return result
    }

    fun levelUp() {
        db.collection("userData")
            .document(auth.uid!!)
            .update("level", FieldValue.increment(1))
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
                        val streak = it.toObject(UserStreak::class.java)
                        view?.onLoadDataSuccess(results, streak!!)
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
        db.collection("userStreak")
            .document(auth.uid!!)
            .update("totalWater", FieldValue.increment(waterAmt.toLong()))
            .addOnSuccessListener {
                        view?.onAddWaterSuccess()
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

    fun addPoints(pointGot: Int) {
        db.collection("userBalance")
            .document(auth.uid!!)
            .update("balance", FieldValue.increment(pointGot.toLong()))
    }
}