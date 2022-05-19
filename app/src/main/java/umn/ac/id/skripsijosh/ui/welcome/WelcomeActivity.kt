package umn.ac.id.skripsijosh.ui.welcome

import android.content.Intent
import android.os.Bundle
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityWelcomeBinding
import umn.ac.id.skripsijosh.pojo.RegistDone
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

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun startLoading() {
        if (checkIfActivityFinished()) return
        showLoadingProgress()
    }

    override fun stopLoading() {
        if (checkIfActivityFinished()) return
        dismissLoading()
    }

    override fun showError(message: String) {}

    override fun showEmpty() {}

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RegistDone) {
        finish()
    }
}