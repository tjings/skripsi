package com.example.skripsijosh.ui.medals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsijosh.base.BaseFragment
import com.example.skripsijosh.databinding.FragmentMedalsBinding
import com.example.skripsijosh.pojo.UserMedal
import com.google.firebase.firestore.auth.User


class MedalsFragment : BaseFragment(), MedalsView {
    private lateinit var presenter: MedalsPresenter
    private lateinit var binding: FragmentMedalsBinding
    private var medalDetails: MutableList<UserMedal> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        presenter = MedalsPresenter(this)
        binding = FragmentMedalsBinding.inflate(layoutInflater)
        presenter.getUserMedals()
        return binding.root
    }

    override fun onGetMedalSuccess(medalDeets: UserMedal) {
        medalDetails.add(medalDeets)
        Log.d("medalname", medalDeets.name.toString())
        Log.d("medaldeets", medalDeets.desc.toString())
        initAdapter()
    }

    override fun startLoading() {}

    override fun stopLoading() {}

    override fun showError(message: String) { }

    override fun showEmpty() {}

    private fun initAdapter() {
        binding.rvMedals.adapter = MedalsAdapter(medalDetails)
        binding.rvMedals.layoutManager = LinearLayoutManager(context)
    }

}