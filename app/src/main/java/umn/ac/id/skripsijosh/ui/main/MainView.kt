package umn.ac.id.skripsijosh.ui.main

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.UserDailyWater
import umn.ac.id.skripsijosh.pojo.UserData

interface MainView : BaseView {
    fun onGetDataUserSucces(userData: UserData)
}