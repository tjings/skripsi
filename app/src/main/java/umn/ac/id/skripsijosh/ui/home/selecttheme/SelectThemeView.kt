package umn.ac.id.skripsijosh.ui.home.selecttheme

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.UserInventory

interface SelectThemeView : BaseView {
    fun onGetInventorySuccess(result: UserInventory)
}