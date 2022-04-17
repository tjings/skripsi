package com.example.skripsijosh.ui.home

import android.util.Log
import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.UserWater


class HomePresenter (view: HomeView) : BasePresenter <HomeView>() {
    init {
        super.attachView(view)
    }

    fun getWaterData() {
        view?.startLoading()
        db.collection("userWater")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                view?.stopLoading()
                if(it.exists()) {
                    val result: UserWater = it.toObject(UserWater::class.java)!!
                    view?.onLoadDataSuccess(result.progressDaily)
                }
            }
            .addOnFailureListener {
                view?.stopLoading()
                view?.showError(it.message.toString())
            }
    }

    fun addWater(waterAmt: Int) {
        view?.startLoading()
        val updatedWater = UserWater(
            progressDaily = waterAmt
        )
        Log.d("test", waterAmt.toString())
        db.collection("userWater")
            .document(auth.uid!!)
            .set(updatedWater)
            .addOnSuccessListener {
                view?.stopLoading()
                view?.onAddWaterSuccess()
            }
            .addOnFailureListener {
                view?.stopLoading()
                view?.showError(it.message.toString())
            }
    }
}
