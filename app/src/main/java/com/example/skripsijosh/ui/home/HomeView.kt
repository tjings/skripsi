package com.example.skripsijosh.ui.home

import com.example.skripsijosh.base.BaseView
import com.example.skripsijosh.pojo.UserDailyWater
import com.example.skripsijosh.pojo.UserStreak

interface HomeView : BaseView {
    fun onAddWaterSuccess()
    fun onLoadDataSuccess(results: MutableList<UserDailyWater>, streak: UserStreak)
}