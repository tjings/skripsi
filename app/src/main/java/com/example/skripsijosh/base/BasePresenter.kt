package com.example.skripsijosh.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class BasePresenter <V> {
    var view: V? = null
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val db : FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    fun attachView(view: V) {
        this.view = view
    }

    fun dettachView() {
        this.view = null
    }

}
