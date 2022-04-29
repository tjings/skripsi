package com.example.skripsijosh.ui.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference


class TimePreference(ctxt: Context?, attrs: AttributeSet?) : DialogPreference(ctxt!!, attrs) {
    var hour: Int = 0
    var minute = 0

    fun parseHour(value: String): Int {
        return try {
            val time = value.split(":").toTypedArray()
            time[0].toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun parseMinute(value: String): Int {
        return try {
            val time = value.split(":").toTypedArray()
            time[1].toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun timeToString(h: Int, m: Int): String {
        return String.format("%02d", h) + ":" + String.format("%02d", m)
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any? {
        return a.getString(index)
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
        val value: String
        if (restoreValue) {
            if (defaultValue == null) value = getPersistedString("00:00") else value =
                getPersistedString(defaultValue.toString())
        } else {
            value = defaultValue.toString()
        }
        hour = parseHour(value)
        minute = parseMinute(value)
    }

    fun persistStringValue(value: String?) {
        persistString(value)
    }
}