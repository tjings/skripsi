package com.example.skripsijosh.ui.register.biodata

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.skripsijosh.R
import com.example.skripsijosh.base.BaseActivity
import com.example.skripsijosh.databinding.ActivityBiodataBinding
import com.example.skripsijosh.ui.main.MainActivity
import com.example.skripsijosh.utils.Util
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BiodataActivity : BaseActivity(), BiodataView {

    private lateinit var binding : ActivityBiodataBinding
    private lateinit var presenter: BiodataPresenter
    private var displayName: String? = null
    private var bday: String? = null
    private var weight: String? = null
    private var height: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = BiodataPresenter(this)

        if(currentUser == null) {
            finish()
        }

        val gender = resources.getStringArray(R.array.Gender)
        binding.etGender.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, gender)
        binding.etBday.transformIntoDatePicker(this, "MM/dd/yyyy", Date())

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

    override fun onBackPressed() {
        finish()
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

    private fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val tempDate = if (Util.isNotNull(binding.etBday.text.toString())) {
            val formattingToDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                formattingToDate.parse(binding.etBday.text.toString())
            } catch (e: ParseException) {
                val calendar = Calendar.getInstance()
                calendar.set(1990, 0, 1)
                calendar.time
            }
        } else {
            val calendar = Calendar.getInstance()
            calendar.set(1990, 0, 1)
            calendar.time
        }
        val calendar = Calendar.getInstance()

        if (tempDate != null) {
            calendar.time = tempDate
        }

        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(calendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, R.style.Theme_Skripsijosh_DatePicker, datePickerOnDataSetListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
}