package com.example.skripsijosh.ui.register.biodata

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
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

    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
}