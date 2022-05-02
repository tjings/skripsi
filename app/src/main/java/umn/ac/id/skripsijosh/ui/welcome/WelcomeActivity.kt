package umn.ac.id.skripsijosh.ui.welcome

import android.content.Intent
import android.os.Bundle
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityWelcomeBinding
import umn.ac.id.skripsijosh.ui.login.LoginActivity
import umn.ac.id.skripsijosh.ui.main.MainActivity
import umn.ac.id.skripsijosh.ui.register.RegisterActivity

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