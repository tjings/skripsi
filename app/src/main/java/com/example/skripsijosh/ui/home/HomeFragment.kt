package com.example.skripsijosh.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skripsijosh.R
import com.example.skripsijosh.base.BaseFragment
import com.example.skripsijosh.databinding.FragmentHomeBinding
import com.example.skripsijosh.ui.challenge.ChallengeActivity
import com.example.skripsijosh.ui.shop.ShopActivity
import com.example.skripsijosh.utils.DialogUtil
import java.util.*


class HomeFragment : BaseFragment(), HomeView {
    private lateinit var presenter: HomePresenter
    private lateinit var binding: FragmentHomeBinding
    var mWaterProgress = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        presenter = HomePresenter(this)
        presenter.getWaterData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        binding.srlHome.setOnRefreshListener {
            presenter.getWaterData()
        }
        binding.rlToChallenge.setOnClickListener {
            startActivity(Intent(context, ChallengeActivity::class.java))
        }
        binding.rltoShop.setOnClickListener {
            startActivity(Intent(context, ShopActivity::class.java))
        }
        binding.tvDayAndDate.text = DateFormat.format("EEEE, dd MMM yyyy" , Date()) as String
        binding.fabAdd.setOnClickListener {
            DialogUtil(requireActivity()).showDialog(
                callback = object : DialogUtil.DialogActionCallback{
                    override fun onPositive(waterAmt: Int?) {
                        val sendWater = mWaterProgress + waterAmt!!
                        presenter.addWater(sendWater)
                    }
                    override fun onNegative() {
                    }
                }
            )
        }
    }

    override fun onLoadDataSuccess(result: Int) {
        mWaterProgress = result
        binding.tvProgressML.text = String.format(getString(R.string.drank_water), mWaterProgress)
        val progress = (mWaterProgress * 100) / 2000
        binding.progressBar.progress = if(progress < 100) {
            progress
        } else 100
        binding.tvHomeProgress.text = if(progress < 100) {
            "$progress%"
        } else "100%"
    }

    override fun startLoading() {
        showLoadingProgressOnly()
    }

    override fun stopLoading() {
        binding.srlHome.isRefreshing = false
        dismissLoadingProgress()
    }

    override fun showError(message: String) {}

    override fun showEmpty() {}

    override fun onAddWaterSuccess() {
        presenter.getWaterData()
    }
}