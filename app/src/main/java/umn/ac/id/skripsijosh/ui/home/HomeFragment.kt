package umn.ac.id.skripsijosh.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseFragment
import umn.ac.id.skripsijosh.databinding.FragmentHomeBinding
import umn.ac.id.skripsijosh.pojo.UserDailyWater
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.pojo.UserStreak
import umn.ac.id.skripsijosh.ui.challenge.ChallengeActivity
import umn.ac.id.skripsijosh.ui.shop.ShopActivity
import umn.ac.id.skripsijosh.utils.DialogUtil
import umn.ac.id.skripsijosh.utils.Util
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : BaseFragment(), HomeView {
    private lateinit var presenter: HomePresenter
    private lateinit var binding: FragmentHomeBinding
    private  var userName = ""
    private var date: CharSequence = ""
    private var time: String = ""
    private var mLatestStreak: CharSequence = ""
    private var mWaterProgress = 0
    private var mStreak = 0
    private var mTotalStreak = 0
    private var progress: Int = 0
    private var isStreakBroken = false
    private var isCompleteDaily = false
    private var isTodayStreaked = false

    private val timer = Timer()
    private val task: TimerTask = object : TimerTask() {
        override fun run() {
            date = DateFormat.format("ddMMMyy", Date())
            time = DateFormat.format("HHmmss", Date()) as String
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        presenter = HomePresenter(this)
        date = DateFormat.format("ddMMMyy", Date())
        time = DateFormat.format("HHmmss", Date()) as String
        presenter.getWaterData(date.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        timer.schedule(task, 0L, 1000 * 30)
        binding.srlHome.setOnRefreshListener {
            presenter.getWaterData(today = date.toString())
        }
        binding.rlToChallenge.setOnClickListener {
            startActivity(Intent(context, ChallengeActivity::class.java))
        }
        binding.rltoShop.setOnClickListener {
            startActivity(Intent(context, ShopActivity::class.java))
        }
        binding.tvDayAndDate.text = DateFormat.format("EEEE, dd MMMM yyyy" , Date()) as String
        binding.fabAdd.setOnClickListener {
            DialogUtil(requireActivity()).showDialog(
                callback = object : DialogUtil.DialogActionCallback{
                    override fun onPositive(waterAmt: Int?) {
                        presenter.addWater(today = date.toString(), time = time, waterAmt = waterAmt!!)
                    }
                    override fun onNegative() {
                    }
                }
            )
        }
    }

    override fun onLoadDataSuccess(results: MutableList<UserDailyWater>, streak: UserStreak) {
        if (checkIfFragmentNotAttachToActivity()) return
        mWaterProgress = 0
        results.forEach {
            mWaterProgress += it.dailyWater
        }
        mStreak = streak.streak
        mTotalStreak = streak.highestStreak!!
        mLatestStreak = streak.latestDate

        if(Util.isNotNull(mLatestStreak)) {
            val from = LocalDate.parse(mLatestStreak, DateTimeFormatter.ofPattern("ddMMMyy"))
            val today = LocalDate.now()
            val period = Period.between(from, today)
            if (period.days > 1 || period.days < 0) {
                isStreakBroken = true
            }
            if (period.days == 0) {
                isTodayStreaked = true
            }
        }
        presenter.loadUserData()
        progress = (mWaterProgress * 100) / 2000
        when {
            progress <= 0 -> {
                binding.progressBar.progress = 0
                binding.tvHomeProgress.text = String.format(getString(R.string.progress), 0)
            }
            progress < 100 ->{
                binding.progressBar.progress = progress
                binding.tvHomeProgress.text = String.format(getString(R.string.progress), progress)
            }
            else -> {
                isCompleteDaily = true
                binding.progressBar.progress = 100
                binding.tvHomeProgress.text = String.format(getString(R.string.progress), 100)
                setStreak()
            }
        }
    }

    override fun onSuccessLoadProfile(userData: UserData) {
        if (checkIfFragmentNotAttachToActivity()) return
        userName = userData.displayName.toString()
        binding.tvProgressML.text = String.format(getString(R.string.drank_water), userName, mWaterProgress)
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
        if (checkIfFragmentNotAttachToActivity()) return
        date = DateFormat.format("ddMMMyy", Date()) as String
        presenter.getWaterData(today = date.toString())
    }

    private fun setStreak() {
        if (!isTodayStreaked) {
            val userStreak = if (isStreakBroken) {
                if (isCompleteDaily) {
                    if (mTotalStreak < mStreak) {
                        UserStreak(
                            userName = userName,
                            streak = 1,
                            highestStreak = mStreak,
                            latestDate = date.toString()
                        )
                    } else {
                        UserStreak(
                            userName = userName,
                            streak = 1,
                            highestStreak = mTotalStreak,
                            latestDate = date.toString()
                        )
                    }
                } else {
                    UserStreak(
                        userName = userName,
                        streak = 0,
                        highestStreak = mTotalStreak,
                        latestDate = mLatestStreak.toString()
                    )
                }
            } else {
                if (mStreak < mTotalStreak) {
                    UserStreak(
                        userName = userName,
                        streak = mStreak + 1,
                        highestStreak = mTotalStreak,
                        latestDate = date.toString()
                    )
                } else {
                    UserStreak(
                        userName = userName,
                        streak = mStreak + 1,
                        highestStreak = mStreak + 1,
                        latestDate = date.toString()
                    )
                }
            }
            presenter.setStreak(userStreak)
            isTodayStreaked = true
        }
    }
}