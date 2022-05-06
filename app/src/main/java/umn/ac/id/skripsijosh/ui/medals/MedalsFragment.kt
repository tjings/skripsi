package umn.ac.id.skripsijosh.ui.medals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import umn.ac.id.skripsijosh.base.BaseFragment
import umn.ac.id.skripsijosh.databinding.FragmentMedalsBinding
import umn.ac.id.skripsijosh.pojo.UserMedal


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
        if (checkIfFragmentNotAttachToActivity()) return
        medalDetails.add(medalDeets)
        Log.d("medalname", medalDeets.name)
        Log.d("medaldeets", medalDeets.desc)
        initAdapter()
    }

    override fun startLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
        showLoadingProgressOnly()
    }

    override fun stopLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
        dismissLoadingProgress()
    }

    override fun showError(message: String) { }

    override fun showEmpty() {}

    private fun initAdapter() {
        binding.rvMedals.adapter = MedalsAdapter(medalDetails)
        binding.rvMedals.layoutManager = LinearLayoutManager(context)
    }

}