package com.example.skripsijosh.ui.welcome

import android.content.Intent
import android.os.Bundle
import com.example.skripsijosh.base.BaseActivity
import com.example.skripsijosh.databinding.ActivityWelcomeBinding
import com.example.skripsijosh.ui.login.LoginActivity
import com.example.skripsijosh.ui.main.MainActivity
import com.example.skripsijosh.ui.register.RegisterActivity

class WelcomeActivity : BaseActivity(), WelcomeView {
    private lateinit var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(currentUser !=  null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun startLoading() {}

    override fun stopLoading() {}

    override fun showError(message: String) {}

    override fun showEmpty() {}
}