package com.example.skripsijosh.ui.register.biodata

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import com.example.skripsijosh.base.BaseActivity
import com.example.skripsijosh.databinding.ActivityBiodataBinding
import com.example.skripsijosh.ui.main.MainActivity
import com.example.skripsijosh.utils.Util
import java.text.SimpleDateFormat
import java.util.*

class BiodataActivity : BaseActivity(), BiodataView {

    private lateinit var binding : ActivityBiodataBinding
    private lateinit var presenter: BiodataPresenter
    private var displayName: String? = null
    private var bday: String? = null
    private var weight: String? = null
    private var height: String? = null
    private var datePickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = BiodataPresenter(this)

        if(currentUser == null) {
            finish()
        }

        binding.etBday.setOnClickListener {
            datePicker()
        }

        binding.btnSave.setOnClickListener {
            saveUserData()
        }
    }

    override fun onSaveBiodataSucces() {
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

    private fun saveUserData() {
        displayName = binding.etName.text.toString()
        bday = binding.etBday.text.toString()
        weight = binding.etWeight.text.toString()
        height = binding.etHeight.text.toString()

        presenter.addUserData(
            displayName = displayName!!,
            bday = bday!!,
            weight = weight!!,
            height = height!!,
            displayPic = null
        )
    }

    private fun datePicker() {
        Util.hideKeyboard(this, binding.etBday)
        if(datePickerDialog == null) {
            val tempDate = if(Util.isNotNull(binding.etBday.toString())) {
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                simpleDateFormat.parse(binding.etBday.toString())
            } else {
                val date = Calendar.getInstance()
                date.set(1990, 0, 1)
                date.time
            }
            val date = Calendar.getInstance()
            date.time = tempDate!!
            val bYear = date.get(Calendar.YEAR)
            val bMonth = date.get(Calendar.MONTH)
            val bDate = date.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(this, { datePicker, i, i2, i3 ->
                val datePicked = "$i3-${i2+1}-$i"
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val userBday = simpleDateFormat.format(datePicked)
                binding.etBday.setText(userBday)
            }, bYear, bMonth, bDate)
            datePickerDialog!!.datePicker.maxDate = System.currentTimeMillis()
        }
        datePickerDialog!!.show()
    }
}