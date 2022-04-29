package umn.ac.id.skripsijosh.ui.customviews

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import umn.ac.id.skripsijosh.R

class CustomProgressDialog : DialogFragment() {

    private var isDialogCancelable: Boolean = true

    companion object {
        fun getInstance(isCancelable: Boolean): CustomProgressDialog {
            return CustomProgressDialog().apply {
                isDialogCancelable = isCancelable
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.progress_circular, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressCircular)
        val selectedColor = ContextCompat.getColor(view.context, R.color.red)
        progressBar.indeterminateDrawable.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN)
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.setCancelable(isDialogCancelable)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (this.isAdded) return
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
            manager.executePendingTransactions()
        } catch (e: IllegalStateException) {
            Log.e("CustomProgressDialog", "Error on showing CustomProgressDialog DialogFragment", e)
        }
    }
}