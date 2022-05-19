package umn.ac.id.skripsijosh.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityLoginBinding
import umn.ac.id.skripsijosh.pojo.Logout
import umn.ac.id.skripsijosh.pojo.RegistDone
import umn.ac.id.skripsijosh.ui.main.MainActivity
import umn.ac.id.skripsijosh.ui.register.RegisterActivity
import umn.ac.id.skripsijosh.ui.welcome.WelcomeActivity
import umn.ac.id.skripsijosh.utils.Util

class LoginActivity : BaseActivity(), LoginView {
    private lateinit var binding : ActivityLoginBinding
    lateinit var presenter : LoginPresenter
    var email: String? = null
    var password: String? = null
    var isEmailValid: Boolean? = null
    var isPassValid: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = LoginPresenter(this)
        registerText()

        binding.btnLogin.setOnClickListener {
            onLoginClicked()
        }

        if(currentUser != null) {
            onLoginSuccesful()
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

    override fun onLoginSuccesful() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun startLoading() {
        if (checkIfActivityFinished()) {
            return
        }
        showLoadingProgress()
    }

    override fun stopLoading() {
        if (checkIfActivityFinished()) {
            return
        }
        dismissLoading()
    }

    override fun showError(message: String) {
        if (checkIfActivityFinished()) {
            return
        }
    }

    override fun showEmpty() {}

    private fun onLoginClicked() {
        email = binding.tiEdtEmail.text.toString()
        password = binding.tiEdtPass.text.toString()
        if (Util.isNotNull(email) && Util.isValidEmail(email!!)) {
            isEmailValid = true
        }
        if (Util.isNotNull(password) && password!!.length > 6) {
            isPassValid = true
        }
        if(isEmailValid == true && isPassValid == true) {
            presenter.verifyUser(activity =  this, email = email!!, password = password!!)
        } else {
            Toast.makeText(this, "Ada yang salah", Toast.LENGTH_LONG).show()
        }
    }

    fun registerText() {
        val noAcc = "Haven't an account yet? "
        val regist = "Register"
        val here = "here"

        val spanNoAcc = SpannableString(noAcc)
        val spanReg = SpannableString(regist)
        val spanHere = SpannableString(here)

        val black = ContextCompat.getColor(this, R.color.black)

        val clickHere = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent (this@LoginActivity, RegisterActivity::class.java))
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = black
                ds.isUnderlineText = false
            }
        }

        spanHere.setSpan(clickHere,
            0,
            here.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val completeText = TextUtils.concat(spanNoAcc, spanReg, " ", spanHere)
        binding.tvRegister.text = completeText
        binding.tvRegister.movementMethod = LinkMovementMethod.getInstance()
        binding.tvRegister.highlightColor = Color.TRANSPARENT
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RegistDone) {
        finish()
    }
}