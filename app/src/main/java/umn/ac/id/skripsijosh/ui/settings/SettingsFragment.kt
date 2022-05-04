
package umn.ac.id.skripsijosh.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.ui.customviews.TimePreference
import umn.ac.id.skripsijosh.ui.customviews.TimePreferenceDialogFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val notificationPreference = findPreference<ListPreference>(getString(R.string.key_enable_notif)) as ListPreference
        notificationPreference.setOnPreferenceChangeListener { preference, newValue ->
            if(preference is ListPreference) {
                val index = preference.findIndexOfValue(newValue.toString())
                when (preference.entries[index]){
                    "Disable" -> {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("enable_notif", false)
                        editor.apply()
                    }
                    "Enable" -> {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("enable_notif", true)
                        editor.apply()
                    }
                }
            }
            true
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        var dialogFragment: DialogFragment? = null
        if (preference is TimePreference) {
            dialogFragment = TimePreferenceDialogFragmentCompat()
            val bundle = Bundle(1)
            bundle.putString("key", preference.getKey())
            dialogFragment.setArguments(bundle)
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(
                this.requireFragmentManager(),
                "android.support.v7.preference.PreferenceFragment.DIALOG"
            )
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}