package umn.ac.id.skripsijosh.utils

import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import umn.ac.id.skripsijosh.R

class DialogUtil(private val activity: umn.ac.id.skripsijosh.base.BaseActivity) {

    constructor(fragmentActivity: FragmentActivity): this(fragmentActivity as umn.ac.id.skripsijosh.base.BaseActivity)

    private fun checkIfActivityFinished(): Boolean {
        return activity.isFinishing ||
                activity.isDestroyed
    }

    private fun dismissStaticMaterialDialog() {
        if (activity.mDialog?.isShowing == true) {
            try {
                activity.mDialog!!.dismiss()
            } catch (e: IllegalArgumentException) {
                Log.e("DialogUtil", "Error on dismissing the last material dialog", e)
            }
        }
        activity.mDialog = null
    }

    fun showDialog(callback: DialogActionCallback) {
        dismissStaticMaterialDialog()
        if (checkIfActivityFinished()) return
        activity.mDialog = MaterialDialog(activity)
            .customView(R.layout.dialog_prom, scrollable = false)
            .cornerRadius(activity.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp).toFloat())

        val customView = activity.mDialog?.getCustomView()
        val btnDialogSubmit = customView?.findViewById<AppCompatButton>(R.id.btnSubmit)
        val btnDialogClose = customView?.findViewById<AppCompatButton>(R.id.btnClose)
        val etWaterAmt = customView?.findViewById<EditText>(R.id.etWaterAmt)
        var waterAmt: String? = "0"

        btnDialogSubmit?.setOnClickListener {
            if(Util.isNotNull(etWaterAmt!!.text) && Util.isPositiveNumber(etWaterAmt.text)) {
                waterAmt = etWaterAmt.text.toString()
            }
            callback.onPositive(waterAmt = waterAmt!!.toInt())
            activity.mDialog?.dismiss()
        }

        btnDialogClose?.setOnClickListener {
            callback.onNegative()
            activity.mDialog?.dismiss()
        }
        activity.mDialog?.show()
    }


    interface DialogActionCallback {
        fun onPositive(waterAmt: Int?)
        fun onNegative()
    }

}