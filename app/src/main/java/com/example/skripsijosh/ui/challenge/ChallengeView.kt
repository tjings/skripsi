package com.example.skripsijosh.ui.challenge

import com.example.skripsijosh.base.BaseView
import com.example.skripsijosh.pojo.ChallengeDetails
import com.example.skripsijosh.pojo.UserStreak
import com.example.skripsijosh.pojo.UserWater

interface ChallengeView : BaseView {
    fun onGetChallengeSuccess(result: MutableList<ChallengeDetails>)
    fun onGetUserMilestonesSuccess(mStreak: UserStreak, mWater: UserWater) {
    }
}