package umn.ac.id.skripsijosh.ui.shop

import umn.ac.id.skripsijosh.base.BaseView
import umn.ac.id.skripsijosh.pojo.ShopItem
import umn.ac.id.skripsijosh.pojo.UserBalance
import umn.ac.id.skripsijosh.pojo.UserInventory

interface ShopView : BaseView {
    fun onGetItemSuccess(result: MutableList<ShopItem>)
    fun onGetBalanceSuccess(result: UserBalance)
    fun showSuccess()
    fun onGetInventorySuccess(result: UserInventory)
}