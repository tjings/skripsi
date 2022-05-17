package umn.ac.id.skripsijosh.ui.leaderboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import umn.ac.id.skripsijosh.base.BaseFragment
import umn.ac.id.skripsijosh.databinding.ActivityLeaderboardBinding
import umn.ac.id.skripsijosh.pojo.UserStreak

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
        if (checkIfFragmentNotAttachToActivity()) return
        leaderboardData.addAll(streak)
        initAdapter()
    }

    override fun startLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
        showLoadingProgressOnly()
    }

    override fun stopLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
        dismissLoadingProgress()}

    override fun showError(message: String) {
        if (checkIfFragmentNotAttachToActivity()) return
        Log.d("error", message)
    }

    override fun showEmpty() {}

    private fun initAdapter() {
        binding.rvLeaderboard.adapter = LeaderboardAdapter(leaderboardData , requireContext())
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(context)
    }
}