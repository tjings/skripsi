package com.example.skripsijosh.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skripsijosh.base.BaseFragment
import com.example.skripsijosh.databinding.ActivityLeaderboardBinding
import com.example.skripsijosh.pojo.UserStreak

class ShopFragment : BaseFragment(), LeaderboardView {
    private lateinit var presenter: LeaderboardPresenter
    private lateinit var binding: ActivityLeaderboardBinding
    private var leaderboardData: MutableList<UserStreak> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        presenter = LeaderboardPresenter(this)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        presenter.getLeaderBoard()
        return binding.root
    }

    override fun onLoadDataSuccess(streak: MutableList<UserStreak>) {
        leaderboardData.addAll(streak)
        initAdapter()
    }

    override fun startLoading() {}

    override fun stopLoading() {}

    override fun showError(message: String) {}

    override fun showEmpty() {}

    private fun initAdapter() {
        binding.rvLeaderboard.adapter = LeaderboardAdapter(leaderboardData , context)
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(context)
    }
}