package umn.ac.id.skripsijosh.ui.home.selecttheme

import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.ShopItem
import umn.ac.id.skripsijosh.pojo.UserInventory

class SelectThemePresenter(view: SelectThemeView) : BasePresenter<SelectThemeView>() {

    init {
        super.attachView(view)
    }

    fun getItemList() {
        view?.startLoading()
        db.collection("userInventory")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                view?.stopLoading()
                val result = it.toObject(UserInventory::class.java)
                view?.onGetInventorySuccess(result!!)
            }
    }
}