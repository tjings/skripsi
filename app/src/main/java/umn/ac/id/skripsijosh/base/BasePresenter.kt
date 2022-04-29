package umn.ac.id.skripsijosh.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

open class BasePresenter <V> {
    var view: V? = null
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser


    val storageReference: StorageReference
        get() = FirebaseStorage.getInstance().reference.child("uploads")

    val db : FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    fun attachView(view: V) {
        this.view = view
    }

    fun dettachView() {
        this.view = null
    }

}
