package umn.ac.id.skripsijosh.ui.shop

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
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
            .orderBy("reqLevel")
            .orderBy("itemPrice")
            .get()
            .addOnSuccessListener {
                val result = it.toObjects(ShopItem::class.java)
                view?.onGetItemSuccess(result)
            }
            .addOnFailureListener {
                view?.stopLoading()
                view?.showError(it.message.toString())
            }
    }

    fun getUserBalance() {
        db.collection("userBalance")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                view?.stopLoading()
                val result = it.toObject(UserBalance::class.java)!!
                view?.onGetBalanceSuccess(result)
            }
            .addOnFailureListener { }
    }

    fun purchaseItem(itemId: String, balanceLeft: Int) {
        view?.startLoading()
        db.collection("userBalance")
            .document(auth.uid!!)
            .update("balance", FieldValue.increment(balanceLeft.toLong()))
            .addOnSuccessListener {
                db.collection("userInventory")
                    .document(auth.uid!!)
                    .update("itemHave", FieldValue.arrayUnion(itemId))
                    .addOnSuccessListener {
                        view?.stopLoading()
                        getUserBalance()
                        getItemList()
                        view?.showSuccess()
                    }
            }
            .addOnFailureListener {}
    }

    fun getInventoryList() {
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