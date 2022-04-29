package umn.ac.id.skripsijosh.ui.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import umn.ac.id.skripsijosh.databinding.ActivityShopBinding

class ShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}