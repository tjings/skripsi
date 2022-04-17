package com.example.skripsijosh.ui.profile

import com.example.skripsijosh.base.BaseView
import com.example.skripsijosh.pojo.UserData

interface ProfileView : BaseView {
    fun onSuccessLoadProfile(userData: UserData)
}