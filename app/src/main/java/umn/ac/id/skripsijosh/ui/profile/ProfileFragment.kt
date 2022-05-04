package umn.ac.id.skripsijosh.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import umn.ac.id.skripsijosh.base.BaseFragment
import umn.ac.id.skripsijosh.databinding.FragmentProfileBinding
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.ui.welcome.WelcomeActivity
import androidx.fragment.app.FragmentTransaction
import com.squareup.picasso.Picasso
import umn.ac.id.skripsijosh.ui.profile.uploadimage.UploadImageActivity
import umn.ac.id.skripsijosh.ui.settings.SettingsActivity
import umn.ac.id.skripsijosh.utils.Util

class ProfileFragment : BaseFragment(), ProfileView {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var presenter: ProfilePresenter
    private var shouldRefreshOnResume = false
    private var isFabVisible: Boolean = false
    private var mUserData: UserData = UserData()
    private var name = ""
    private var height = ""
    private var weight = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        presenter = ProfilePresenter(this)
        init()
        presenter.loadUserData()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        shouldRefreshOnResume = true
    }

    override fun onStop() {
        super.onStop()
        shouldRefreshOnResume = true
    }

    override fun onResume() {
        super.onResume()
        if (shouldRefreshOnResume) {
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            ft.detach(this).attach(this).commit()
        }
    }

    private fun init() {
        binding.notifFab.visibility = View.GONE
        binding.notifFabText.visibility = View.GONE
        binding.logoutFab.visibility = View.GONE
        binding.logoutFabText.visibility = View.GONE

        binding.ivProfile.setOnClickListener{
            startActivity(Intent(requireContext(), UploadImageActivity::class.java))
        }

        binding.addFab.setOnClickListener {
            if(!isFabVisible) {
                binding.notifFab.show()
                binding.logoutFab.show()
                binding.notifFabText.visibility = View.VISIBLE
                binding.logoutFabText.visibility = View.VISIBLE
                isFabVisible = true
            } else {
                binding.notifFab.hide()
                binding.logoutFab.hide()
                binding.notifFabText.visibility = View.GONE
                binding.logoutFabText.visibility = View.GONE
                isFabVisible = false
            }
        }

        binding.notifFab.setOnClickListener {
            startActivity(Intent(context, SettingsActivity::class.java))
        }

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

        binding.logoutFab.setOnClickListener {
            auth.signOut()
            requireActivity().finish()
            startActivity(Intent(context, WelcomeActivity::class.java))
        }

        binding.btnSave.setOnClickListener {
            saveUserChanges()
        }
    }

    private fun saveUserChanges() {
        name = binding.etName.text.toString()
        height = binding.etHeight.text.toString()
        weight = binding.etWeight.text.toString()

        if(!Util.isNotNull(name)) {
            binding.tvErrorName.visibility = View.VISIBLE
        }
        if(!Util.isNotNull(height)) {
            binding.tvErrorHeight.visibility = View.VISIBLE
        }
        if(!Util.isNotNull(weight)) {
            binding.tvErrorWeight.visibility = View.VISIBLE
        }
        else {
            val newUserData = UserData(
                displayName = name,
                gender = mUserData.gender,
                bday = mUserData.bday,
                height = height,
                weight = weight,
                displayPic = mUserData.displayPic,
                isBiodataDone = mUserData.isBiodataDone
            )
            presenter.saveUserData(newUserData)
        }
    }

    override fun onSuccessLoadProfile(userData: UserData) {
        if (checkIfFragmentNotAttachToActivity()) return
        mUserData = userData

        val editor = sharedPreferences.edit()
        editor.putString("display_name", userData.displayName)
        editor.putString("gender", userData.gender)
        editor.putString("bday", userData.bday)
        editor.putString("weight", userData.weight)
        editor.putString("height", userData.height)
        editor.putString("display_pic", userData.displayPic)
        editor.putString("is_biodata_done", userData.isBiodataDone.toString())
        editor.apply()
        Picasso.get()
            .load(userData.displayPic)
            .resize(400, 400)
            .centerCrop()
            .into(binding.ivProfile)
        binding.tvName.text = userData.displayName
        binding.tvEmail.text = auth.currentUser?.email
        binding.etName.setText(userData.displayName)
        binding.etHeight.setText(userData.height)
        binding.etWeight.setText(userData.weight)

    }

    override fun startLoading() {
        showLoadingProgressOnly()
    }

    override fun stopLoading() {
        dismissLoadingProgress()
    }

    override fun showError(message: String) {}

    override fun showEmpty() {}

}