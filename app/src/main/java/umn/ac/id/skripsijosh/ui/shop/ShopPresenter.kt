package umn.ac.id.skripsijosh.ui.shop

import com.google.firebase.firestore.FieldValue
import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.ShopItem
import umn.ac.id.skripsijosh.pojo.UserBalance
import umn.ac.id.skripsijosh.pojo.UserInventory

class ShopPresenter (view: ShopView) : BasePresenter <ShopView>() {

    init {
        super.attachView(view)
    }

    fun getItemList() {
        view?.startLoading()
        db.collection("shopItem")
            .get()
            .addOnSuccessListener {
                view?.stopLoading()
                val result = it.toObjects(ShopItem::class.java)
                view?.onGetItemSuccess(result)
            }
            .addOnFailureListener { }
    }

    fun getUserBalance() {
        db.collection("userBalance")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                val result = it.toObject(UserBalance::class.java)!!
                view?.onGetBalanceSuccess(result)
            }
            .addOnFailureListener { }
    }

    fun purchaseItem(itemId: Int, balanceLeft: Int) {
        view?.startLoading()
        db.collection("userBalance")
            .document(auth.uid!!)
            .update("balance", FieldValue.increment(balanceLeft.toLong()))
            .addOnSuccessListener {
                db.collection("userInventory")
                    .document(auth.uid!!)
                    .update("itemHave", FieldValue.arrayUnion(itemId))
                    .addOnSuccessListener {
                        getUserBalance()
                        getItemList()
                    }
            }
            .addOnFailureListener { }
    }

}