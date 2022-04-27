package com.example.skripsijosh.ui.leaderboard

import com.example.skripsijosh.base.BaseView
import com.example.skripsijosh.pojo.UserStreak

interface LeaderboardView : BaseView {
    fun onLoadDataSuccess(streak: MutableList<UserStreak>)
}