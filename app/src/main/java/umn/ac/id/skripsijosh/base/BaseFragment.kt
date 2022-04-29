package umn.ac.id.skripsijosh.base

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import umn.ac.id.skripsijosh.ui.customviews.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

abstract class BaseFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null
    var progressOnly: CustomProgressDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
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


    fun checkIfFragmentNotAttachToActivity(): Boolean {
        return !isAdded || isDetached || context == null || getActivity() == null
    }

}