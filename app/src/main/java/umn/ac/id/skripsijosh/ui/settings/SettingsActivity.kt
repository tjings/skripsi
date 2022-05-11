package umn.ac.id.skripsijosh.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceManager
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            return
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.idFrameLayout, SettingsFragment())
            .commitAllowingStateLoss()
    }
}