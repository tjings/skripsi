package umn.ac.id.skripsijosh.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager

object Util {

    fun isNotNull(text: String?): Boolean {
        var tempText: String? = text ?: return false
        tempText = tempText!!.trim { it <= ' ' }
        return tempText != ""
    }

    fun isNotNull(text: CharSequence?): Boolean {
        if (text == null) {
            return false
        }
        val newText = text.toString().trim { it <= ' ' }
        return newText != ""
    }

    fun isNotNull(list: List<*>?): Boolean {
        return list != null && list.isNotEmpty()
    }

    fun isNotNull(list: Array<out String?>?): Boolean {
        return list != null && list.isNotEmpty()
    }

    fun isPositiveNumber(char: CharSequence): Boolean {
        val check = char.toString().toInt()
        return when {
            check >= 0 -> true
            check <0 -> false
            else -> false
        }
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun hideKeyboard(activity: Activity, view: View?) {
        if (view == null) return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}