package com.example.skripsijosh.ui.medals

import com.example.skripsijosh.base.BaseView
import com.example.skripsijosh.pojo.UserMedal

interface MedalsView : BaseView {
    fun onGetMedalSuccess(medalDeets: UserMedal)
}