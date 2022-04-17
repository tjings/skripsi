package com.example.skripsijosh.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.skripsijosh.ui.customviews.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

abstract class BaseFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null
    var progressOnly: CustomProgressDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }

    override fun onDetach() {
        super.onDetach()
        this.progressOnly?.dialog?.dismiss()
        this.progressOnly = null
    }

    fun showLoadingProgressOnly(isCancelable: Boolean = true) {
        dismissLoadingProgress()
        activity ?: return
        if (progressOnly == null) {
            progressOnly = CustomProgressDialog.getInstance(isCancelable = isCancelable)
        }
        progressOnly!!.show(childFragmentManager, "CircularProgress")
    }

    fun dismissLoadingProgress() {
        if (progressOnly?.dialog?.isShowing == true) {
            progressOnly?.dialog?.dismiss()
            this.progressOnly = null
        }
    }

}