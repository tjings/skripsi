package umn.ac.id.skripsijosh.utils

import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity

class DialogUtil(private val activity: BaseActivity) {

    constructor(fragmentActivity: FragmentActivity): this(fragmentActivity as BaseActivity)

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
        val tvDialogMssg = customView?.findViewById<TextView>(R.id.tvDialogMssg)
        val btnDialogClose = customView?.findViewById<TextView>(R.id.btnClose)
        val etWaterAmt = customView?.findViewById<EditText>(R.id.etWaterAmt)
        var waterAmt: String? = "0"

        btnDialogSubmit!!.text = activity.getString(R.string.submit)
        btnDialogClose!!.text = activity.getString(R.string.close)
        tvDialogMssg!!.text = activity.getString(R.string.how_much_did_you_just_drink)
        etWaterAmt!!.hint = "Input amount in mililitres"

        btnDialogSubmit.setOnClickListener {
            if(Util.isNotNull(etWaterAmt.text) && Util.isPositiveNumber(etWaterAmt.text)) {
                waterAmt = etWaterAmt.text.toString()
            }
            callback.onPositive(waterAmt = waterAmt!!.toInt())
            activity.mDialog?.dismiss()
        }

        btnDialogClose.setOnClickListener {
            callback.onNegative()
            activity.mDialog?.dismiss()
        }
        activity.mDialog?.show()
    }

    fun showConfirmationDialog(itemName: String, callback: DialogConfirmationCallback) {
        dismissStaticMaterialDialog()
        if (checkIfActivityFinished()) return
        activity.mDialog = MaterialDialog(activity)
            .customView(R.layout.dialog_confirmation, scrollable = false)
            .cornerRadius(activity.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp).toFloat())

        val customView = activity.mDialog?.getCustomView()
        val tvConfirmation = customView?.findViewById<TextView>(R.id.tvDialogMssg)
        val btnConfirm = customView?.findViewById<AppCompatButton>(R.id.btnConfirm)
        val btnCancel = customView?.findViewById<AppCompatButton>(R.id.btnCancel)

        tvConfirmation!!.text = String.format(activity.getString(R.string.are_you_sure), itemName)
        btnConfirm!!.text = activity.getText(R.string.confirm)
        btnCancel!!.text = activity.getText(R.string.cancel)

        btnConfirm.setOnClickListener {
            callback.onPositive()
        }

        btnCancel.setOnClickListener {
            callback.onNegative()
            activity.mDialog?.dismiss()
        }
        activity.mDialog?.show()
    }

    fun showFailed() {
        dismissStaticMaterialDialog()
        if (checkIfActivityFinished()) return
        activity.mDialog = MaterialDialog(activity)
            .customView(R.layout.dialog_failed, scrollable = false)
            .cornerRadius(activity.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp).toFloat())

        val customView = activity.mDialog?.getCustomView()
        val tvConfirmation = customView?.findViewById<TextView>(R.id.tvDialogMssg)
        val btnClose = customView?.findViewById<AppCompatButton>(R.id.btnClose)

        tvConfirmation!!.text = activity.getString(R.string.purchase_failed)
        btnClose?.setOnClickListener {
            activity.mDialog?.dismiss()
        }
        activity.mDialog?.show()
    }



    interface DialogActionCallback {
        fun onPositive(waterAmt: Int?)
        fun onNegative()
    }

    interface DialogConfirmationCallback {
        fun onPositive()
        fun onNegative()
    }

}