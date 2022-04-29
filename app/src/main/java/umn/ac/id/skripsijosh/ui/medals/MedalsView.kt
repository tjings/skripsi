package umn.ac.id.skripsijosh.ui.medals

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.UserMedal

interface MedalsView : BaseView {
    fun onGetMedalSuccess(medalDeets: UserMedal)
}