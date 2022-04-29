package umn.ac.id.skripsijosh.ui.profile

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.UserData

interface ProfileView : BaseView {
    fun onSuccessLoadProfile(userData: UserData)
}