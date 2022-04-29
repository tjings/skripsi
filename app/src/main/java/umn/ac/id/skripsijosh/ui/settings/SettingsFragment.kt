
package umn.ac.id.skripsijosh.ui.settings

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.ui.customviews.TimePreference
import umn.ac.id.skripsijosh.ui.customviews.TimePreferenceDialogFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
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