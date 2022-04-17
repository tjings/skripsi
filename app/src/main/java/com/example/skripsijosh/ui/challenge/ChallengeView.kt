package com.example.skripsijosh.ui.challenge

import com.example.skripsijosh.base.BaseView
import com.example.skripsijosh.pojo.ChallengeDetails

interface ChallengeView : BaseView {
    fun onGetChallengeSuccess(result: MutableList<ChallengeDetails>)
}