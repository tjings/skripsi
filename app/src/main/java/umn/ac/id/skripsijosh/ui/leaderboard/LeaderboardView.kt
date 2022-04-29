package umn.ac.id.skripsijosh.ui.leaderboard

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.UserStreak

interface LeaderboardView : BaseView {
    fun onLoadDataSuccess(streak: MutableList<UserStreak>)
}