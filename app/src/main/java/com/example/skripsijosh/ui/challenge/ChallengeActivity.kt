package com.example.skripsijosh.ui.challenge

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsijosh.R
import com.example.skripsijosh.base.BaseActivity
import com.example.skripsijosh.databinding.ActivityChallengeBinding
import com.example.skripsijosh.pojo.ChallengeDetails
import com.example.skripsijosh.ui.medals.MedalsAdapter

class ChallengeActivity : BaseActivity(), ChallengeView {
    private lateinit var presenter: ChallengePresenter
    private lateinit var binding : ActivityChallengeBinding
    private var challengeList: MutableList<ChallengeDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ChallengePresenter(this)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.getChallengeList()
        binding.include2.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.include2.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onGetChallengeSuccess(result: MutableList<ChallengeDetails>) {
        challengeList.addAll(result)
        initAdapter()
    }

    private fun initAdapter() {
        binding.rvChallenge.adapter = ChallengeAdapter(challengeList)
        binding.rvChallenge.layoutManager = LinearLayoutManager(this)
    }

    override fun startLoading() {}

    override fun stopLoading() {}

    override fun showError(message: String) {}

    override fun showEmpty() {}
}