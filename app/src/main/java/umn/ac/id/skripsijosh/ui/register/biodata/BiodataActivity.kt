package umn.ac.id.skripsijosh.ui.register.biodata

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import org.greenrobot.eventbus.EventBus
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityBiodataBinding
import umn.ac.id.skripsijosh.pojo.Logout
import umn.ac.id.skripsijosh.pojo.RegistDone
import umn.ac.id.skripsijosh.ui.main.MainActivity
import umn.ac.id.skripsijosh.utils.Util
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BiodataActivity : BaseActivity(), BiodataView, AdapterView.OnItemSelectedListener {

    private lateinit var binding : ActivityBiodataBinding
    private lateinit var presenter: BiodataPresenter
    private var displayName: String? = null
    private var bday: String? = null
    private var weight: String? = null
    private var gender: String? = null
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
        binding.spinnerGender.apply {
            adapter = ArrayAdapter(
                this@BiodataActivity,
                android.R.layout.simple_spinner_item, gender
            )
            onItemSelectedListener = this@BiodataActivity

        }
        binding.etBday.transformIntoDatePicker(this, "MM/dd/yyyy", Date())

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val name = binding.etName.text.toString()
                if(name.length < 50) {
                    if (Util.isNotNull(name) && name.length < 4) {
                        binding.tvErrorName.visibility = View.VISIBLE
                        binding.btnSave.isEnabled = false
                    } else {
                        binding.tvErrorName.visibility = View.GONE
                        binding.btnSave.isEnabled = true
                    }
                } else {
                    binding.tvErrorName.visibility = View.VISIBLE
                }
            }
        })

        binding.etBday.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val bday = binding.etBday.text.toString()
                    if (!Util.isNotNull(bday)) {
                        binding.tvErrorBday.visibility = View.VISIBLE
                        binding.btnSave.isEnabled = false
                    } else {
                        binding.tvErrorBday.visibility = View.GONE
                        binding.btnSave.isEnabled = true
                    }
            }
        })

        binding.etHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val height = binding.etHeight.text.toString()
                if (Util.isNotNull(height) && height.length < 2) {
                    binding.tvErrorHeight.visibility = View.VISIBLE
                    binding.btnSave.isEnabled = false
                } else {
                    binding.tvErrorHeight.visibility = View.GONE
                    binding.btnSave.isEnabled = true
                }
            }
        })

        binding.etWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val height = binding.etHeight.text.toString()
                if (Util.isNotNull(height) && height.length < 2) {
                    binding.tvErrorWeight.visibility = View.VISIBLE
                    binding.btnSave.isEnabled = false
                } else {
                    binding.tvErrorWeight.visibility = View.GONE
                    binding.btnSave.isEnabled = true
                }
            }
        })

        binding.btnSave.setOnClickListener {
            saveUserData()
        }
    }

    override fun onSaveBiodataSucces() {
        startActivity(Intent(this, MainActivity::class.java))
        EventBus.getDefault().postSticky(RegistDone())
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


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        gender = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    private fun saveUserData() {
        displayName = binding.etName.text.toString()
        bday = binding.etBday.text.toString()
        weight = binding.etWeight.text.toString()
        height = binding.etHeight.text.toString()

        if(!Util.isNotNull(displayName)) {
            binding.tvErrorName.visibility = View.VISIBLE
        }
        if(!Util.isNotNull(height)) {
            binding.tvErrorHeight.visibility = View.VISIBLE
        }
        if(!Util.isNotNull(weight)) {
            binding.tvErrorWeight.visibility = View.VISIBLE
        }
        if(!Util.isNotNull(bday)) {
            binding.tvErrorBday.visibility = View.VISIBLE
        }
        else {
            presenter.addUserData(
                displayName = displayName!!,
                gender = gender!!,
                bday = bday!!,
                weight = weight!!,
                height = height!!,
                displayPic = null
            )
        }
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
                calendar.set(1998, 0, 1)
                calendar.time
            }
        } else {
            val calendar = Calendar.getInstance()
            calendar.set(1998, 0, 1)
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