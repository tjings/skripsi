package umn.ac.id.skripsijosh.ui.home

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.UserDailyWater
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.pojo.UserStreak

interface HomeView : BaseView {
    fun onAddWaterSuccess()
    fun onLoadDataSuccess(results: MutableList<UserDailyWater>, streak: UserStreak)
    fun onSuccessLoadProfile(userData: UserData)
    fun onAddExpSuccess()
}