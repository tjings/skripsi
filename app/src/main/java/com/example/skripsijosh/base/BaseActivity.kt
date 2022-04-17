package com.example.skripsijosh.base

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.afollestad.materialdialogs.MaterialDialog
import com.example.skripsijosh.ui.customviews.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

abstract class BaseActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null
    var activity: Activity? = null
    var progress: CustomProgressDialog? = null
    var mDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        this.activity = this
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            if (mDialog?.isShowing == true) {
                try {
                    mDialog!!.dismiss()
                } catch (e: IllegalArgumentException) {
                    Log.e("DialogUtil", "Error on dismissing the last material dialog", e)
                }
            }
            mDialog = null
        }
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }

    fun showLoadingProgress(isCancelable: Boolean = true) {
        if (progress == null) {
            progress = CustomProgressDialog.getInstance(isCancelable = isCancelable)
        }
        progress!!.let {
            if (it.isVisible || it.isAdded) return
            it.show(supportFragmentManager, "CircularProgress")
        }
    }

    fun dismissLoading() {
        if (progress?.dialog?.isShowing == true) {
            progress?.dialog?.dismiss()
        }
    }

    fun checkIfActivityFinished(): Boolean {
        return isFinishing ||
                isDestroyed
    }

}