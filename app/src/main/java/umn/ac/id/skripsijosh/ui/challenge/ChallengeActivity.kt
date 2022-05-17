package umn.ac.id.skripsijosh.ui.challenge

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityChallengeBinding
import umn.ac.id.skripsijosh.pojo.ChallengeDetails
import umn.ac.id.skripsijosh.pojo.UserStreak

class ChallengeActivity : BaseActivity(), ChallengeAdapter.ChallengeListener, ChallengeView {
    private lateinit var presenter: ChallengePresenter
    private lateinit var binding : ActivityChallengeBinding
    private var challengeList: MutableList<ChallengeDetails> = arrayListOf()
    private var userStreak = 0
    private var userWater = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ChallengePresenter(this)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.getChallengeList()
        presenter.getMilestones()

        binding.include2.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.include2.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.include2.tvToolbarTitle.text = getString(R.string.challenge)
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
    }

    override fun onGetUserMilestonesSuccess(mStreak: UserStreak) {
        userStreak = mStreak.highestStreak!!
        userWater = mStreak.totalWater!!
        initAdapter()
    }

    override fun startLoading() {
        if (checkIfActivityFinished()) return
        showLoadingProgress()
    }

    override fun stopLoading() {
        if (checkIfActivityFinished()) return
        dismissLoading()
    }

    override fun showError(message: String) {}

    override fun showEmpty() {}

    private fun initAdapter() {
        binding.rvChallenge.adapter = ChallengeAdapter(
            challengeList,
            context = this,
            listener = this@ChallengeActivity,
            userStreak =  userStreak,
            userWater = userWater
        )
        binding.rvChallenge.layoutManager = LinearLayoutManager(this)
    }

    override fun onProgressCompleted(challengeDetails: ChallengeDetails) {
        if(challengeDetails.userCompleted.contains(auth.uid!!)) {
            return
        } else {
            presenter.setUserCompletedChallenge(challengeDetails.challengeId)
            presenter.setMedal(challengeDetails.medalGot)
            presenter.addPoints(challengeDetails.pointGot)
        }
    }
}