package com.example.skripsijosh.ui.main

import com.example.skripsijosh.base.BaseView
import com.example.skripsijosh.pojo.UserData

interface MainView : BaseView {
    fun onGetDataUserSucces(userData: UserData)
}