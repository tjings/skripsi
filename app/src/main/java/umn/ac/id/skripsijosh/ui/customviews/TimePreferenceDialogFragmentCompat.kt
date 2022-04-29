package umn.ac.id.skripsijosh.ui.customviews

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.TimePicker
import androidx.preference.DialogPreference
import androidx.preference.Preference

import androidx.preference.PreferenceDialogFragmentCompat
import androidx.preference.PreferenceManager


class TimePreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat(),
    DialogPreference.TargetFragment {

    private lateinit var sharedPreferences: SharedPreferences
    var timePicker: TimePicker? = null

    override fun onCreateDialogView(context: Context): View? {
        timePicker = TimePicker(context)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return (timePicker)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        timePicker!!.setIs24HourView(true)
        val pref = preference as TimePreference
        timePicker!!.hour = pref.hour
        timePicker!!.minute = pref.minute
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val pref = preference as TimePreference
            pref.hour = timePicker!!.hour
            pref.minute = timePicker!!.minute
            val value = pref.timeToString(pref.hour, pref.minute)
                if(pref.key == "key_wake"){
                    val editor = sharedPreferences.edit()
                    editor.putString("wake_hour", "${timePicker!!.hour}")
                    editor.putString("wake_minute", "${timePicker!!.minute}")
                    editor.apply()
                } else if (pref.key == "key_sleep"){
                    val editor = sharedPreferences.edit()
                    editor.putString("sleep_hour", "${timePicker!!.hour}")
                    editor.putString("sleep_minute", "${timePicker!!.minute}")
                    editor.apply()
                }
            if (pref.callChangeListener(value)) pref.persistStringValue(value)
        }
    }

    override fun <T : Preference?> findPreference(key: CharSequence): T? = preference as T

}