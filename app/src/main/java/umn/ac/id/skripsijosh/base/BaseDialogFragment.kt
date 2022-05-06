package umn.ac.id.skripsijosh.base

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import umn.ac.id.skripsijosh.ui.customviews.CustomProgressDialog

abstract class BaseDialogFragment : DialogFragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var rootView: View
    var progressOnly: CustomProgressDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onDetach() {
        super.onDetach()
        progressOnly?.dialog?.dismiss()
        progressOnly = null
    }

    fun isRootViewInitialized() = ::rootView.isInitialized

    fun showLoadingProgressOnly(isCancelable: Boolean = true) {
        dismissLoadingProgress()
        activity ?: return
        if (progressOnly == null) {
            progressOnly = CustomProgressDialog.getInstance(isCancelable = isCancelable)
        }
        progressOnly!!.let {
            if (it.isVisible || it.isAdded) return
            it.show(childFragmentManager, "CircularProgress")
        }
    }

    fun dismissLoadingProgress() {
        if (progressOnly?.dialog?.isShowing == true) {
            progressOnly?.dialog?.dismiss()
        }
    }

    fun checkIfFragmentNotAttachToActivity(): Boolean {
        return !isAdded || isDetached || activity == null || context == null
    }
}
