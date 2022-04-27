package com.example.skripsijosh.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.skripsijosh.R
import com.example.skripsijosh.base.BaseFragment
import com.example.skripsijosh.databinding.FragmentProfileBinding
import com.example.skripsijosh.pojo.UserData
import com.example.skripsijosh.ui.welcome.WelcomeActivity
import androidx.appcompat.widget.Toolbar
import com.example.skripsijosh.ui.main.MainActivity

class ProfileFragment : BaseFragment(), ProfileView {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var presenter: ProfilePresenter
    private var isFabVisible : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        presenter = ProfilePresenter(this)

        binding.notifFab.visibility = View.GONE
        binding.notifFabText.visibility = View.GONE
        binding.logoutFab.visibility = View.GONE
        binding.logoutFabText.visibility = View.GONE

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

        binding.notifFab.setOnClickListener {  }

        binding.logoutFab.setOnClickListener {
            auth.signOut()
            requireActivity().finish()
            startActivity(Intent(context, WelcomeActivity::class.java))
        }

        presenter.loadUserData()
        return binding.root
    }

    override fun onSuccessLoadProfile(userData: UserData) {
        binding.tvName.text = userData.displayName
        binding.tvEmail.text = auth.currentUser?.email
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