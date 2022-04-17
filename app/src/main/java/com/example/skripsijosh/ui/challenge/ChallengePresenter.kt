package com.example.skripsijosh.ui.challenge

import com.example.skripsijosh.base.BasePresenter
import com.example.skripsijosh.pojo.ChallengeDetails

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
}