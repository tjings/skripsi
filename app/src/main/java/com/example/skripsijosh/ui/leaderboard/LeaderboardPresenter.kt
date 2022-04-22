package com.example.skripsijosh.ui.leaderboard

import com.example.skripsijosh.base.BasePresenter

class LeaderboardPresenter (view: LeaderboardView) : BasePresenter<LeaderboardView>() {

    init {
        super.attachView(view)
    }
}