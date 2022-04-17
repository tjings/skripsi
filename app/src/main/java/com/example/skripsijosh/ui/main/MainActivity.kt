package com.example.skripsijosh.ui.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import com.example.skripsijosh.R
import com.example.skripsijosh.base.BaseActivity
import com.example.skripsijosh.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val startDestinations = mapOf(
        R.id.menuHome to R.id.homeFragment,
        R.id.menuMedals to R.id.medalsFragment,
        R.id.menuProfile to R.id.profileFragment,
        R.id.menuShop to R.id.leaderboardFragment
    )
    private lateinit var binding : ActivityMainBinding
    private lateinit var homeTabContainer : View
    private lateinit var medalTabContainer : View
    private lateinit var shopTabContainer : View
    private lateinit var profileTabContainer : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeTabContainer = binding.homeTab
        medalTabContainer = binding.medalsTab
        profileTabContainer = binding.profileTab
        shopTabContainer = binding.shopTab

        binding.bottomNavigation.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuHome -> {
                Navigation.findNavController(this, R.id.homeTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuHome))
                    }
                }
                makeOtherInvisible(homeTabContainer)
                return true
            }
            R.id.menuMedals -> {
                Navigation.findNavController(this, R.id.medalsTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuMedals))
                    }
                }
                makeOtherInvisible(medalTabContainer)
                return true
            }
            R.id.menuShop -> {
                Navigation.findNavController(this, R.id.shopTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuShop))
                    }
                }
                makeOtherInvisible(shopTabContainer)
                return true
            }
            R.id.menuProfile -> {
                Navigation.findNavController(this, R.id.profileTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuProfile))
                    }
                }
                makeOtherInvisible(profileTabContainer)
                return true
            }
            else -> {
                Log.d("error", "error on navigating tab")
                return false
            }
        }
    }

    private fun makeOtherInvisible(container : View) {
        homeTabContainer.visibility = View.INVISIBLE
        medalTabContainer.visibility = View.INVISIBLE
        profileTabContainer.visibility = View.INVISIBLE
        shopTabContainer.visibility = View.INVISIBLE
        container.visibility = View.VISIBLE
    }
}