package com.example.skripsijosh.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skripsijosh.base.BaseFragment
import com.example.skripsijosh.databinding.FragmentProfileBinding
import com.example.skripsijosh.pojo.UserData
import com.example.skripsijosh.ui.welcome.WelcomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : BaseFragment(), ProfileView {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        presenter = ProfilePresenter(this)

        binding.tvLogout.setOnClickListener {
            auth.signOut()
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