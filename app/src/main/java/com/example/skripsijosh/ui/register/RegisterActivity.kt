package com.example.skripsijosh.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.skripsijosh.base.BaseActivity
import com.example.skripsijosh.databinding.ActivityRegisterBinding
import com.example.skripsijosh.ui.register.biodata.BiodataActivity
import com.example.skripsijosh.utils.Util

class RegisterActivity : BaseActivity(), RegisterView {

    private lateinit var binding : ActivityRegisterBinding
    lateinit var presenter: RegisterPresenter
    var email: String? = null
    var password: String? = null
    var rePassword: String? = null
    var isEmailValid: Boolean? = null
    var isPassValid: Boolean? = null
    var confPass: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = RegisterPresenter(this)

        binding.btnRegister.setOnClickListener {
            onRegisterButtonClicked()
        }

        if(currentUser != null) {
            finish()
        }
    }

    override fun onRegisterSuccess() {
        startActivity(Intent(this, BiodataActivity::class.java))
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

    private fun onRegisterButtonClicked() {
        email = binding.etEmail.text.toString()
        password = binding.etPass.text.toString()
        rePassword = binding.etPass.text.toString()
        if (Util.isNotNull(email) && Util.isValidEmail(email!!)) {
            isEmailValid = true
        }
        if (Util.isNotNull(password) && password!!.length > 6) {
            isPassValid = true
        }
        if(Util.isNotNull(rePassword) && rePassword == password) {
            confPass = true
        }
        if(isEmailValid == true && isPassValid == true && confPass == true) {
            presenter.registerUser(activity = this, email = email, password = password)
        } else {
            Toast.makeText(this, "Ada yang salah", Toast.LENGTH_LONG).show()
        }
    }
}