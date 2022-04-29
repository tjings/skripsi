package umn.ac.id.skripsijosh.ui.challenge

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.ChallengeDetails
import umn.ac.id.skripsijosh.pojo.UserStreak
import umn.ac.id.skripsijosh.pojo.UserWater

interface ChallengeView : BaseView {
    fun onGetChallengeSuccess(result: MutableList<ChallengeDetails>)
    fun onGetUserMilestonesSuccess(mStreak: UserStreak, mWater: UserWater) {
    }
}