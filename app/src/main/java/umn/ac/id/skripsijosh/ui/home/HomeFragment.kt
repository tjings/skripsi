package umn.ac.id.skripsijosh.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayRect
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseFragment
import umn.ac.id.skripsijosh.databinding.FragmentHomeBinding
import umn.ac.id.skripsijosh.pojo.UserDailyWater
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.pojo.UserStreak
import umn.ac.id.skripsijosh.ui.challenge.ChallengeActivity
import umn.ac.id.skripsijosh.ui.home.selecttheme.SelectThemeFragment
import umn.ac.id.skripsijosh.ui.shop.ShopActivity
import umn.ac.id.skripsijosh.utils.DialogUtil
import umn.ac.id.skripsijosh.utils.Util
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


class HomeFragment : BaseFragment(), HomeView, SelectThemeFragment.SelectThemeListener {
    private lateinit var presenter: HomePresenter
    private lateinit var binding: FragmentHomeBinding
    private  var userName = ""
    private var displayPic = ""
    private var date: CharSequence = ""
    private var time: String = ""
    private var mLatestStreak: CharSequence = ""
    private var selectedTheme = ""
    private var mWaterProgress = 0
    private var mStreak = 0
    private var mTotalStreak = 0
    private var progress: Int = 0
    private var level: Int = 0
    private var totalExp: Int = 0
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
        selectedTheme = sharedPreferences.getString("theme_applied", "circle").toString()
        init()
    }

    override fun onLoadDataSuccess(results: MutableList<UserDailyWater>, streak: UserStreak) {
        if (checkIfFragmentNotAttachToActivity()) return
        totalExp = streak.totalWater!!
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
        val editor = sharedPreferences.edit()
        editor.putString("progress", progress.toString())
        editor.apply()
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
        displayPic = userData.displayPic.toString()
        binding.tvProgressML.text = String.format(getString(R.string.drank_water), userName, mWaterProgress)
    }

    override fun onAddExpSuccess() {
        presenter.loadUserData()
    }

    override fun startLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
        showLoadingProgressOnly()
    }

    override fun stopLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
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

    override fun onChangeThemeSuccess(themeId: String) {
        val img: Int = resources.getIdentifier(themeId + "_theme", "drawable", context?.packageName)
        binding.progressBar.progressDrawable = getDrawable(requireContext(), img)
        activity?.recreate()
    }

    private fun setStreak() {
        if (!isTodayStreaked) {
            val userStreak = if (isStreakBroken) {
                if (isCompleteDaily) {
                    if (mTotalStreak < mStreak) {
                        UserStreak(
                            userName = userName,
                            displayPic = displayPic,
                            streak = 1,
                            highestStreak = mStreak,
                            latestDate = date.toString()
                        )
                    } else {
                        UserStreak(
                            userName = userName,
                            displayPic = displayPic,
                            streak = 1,
                            highestStreak = mTotalStreak,
                            latestDate = date.toString()
                        )
                    }
                } else {
                    UserStreak(
                        userName = userName,
                        displayPic = displayPic,
                        streak = 0,
                        highestStreak = mTotalStreak,
                        latestDate = mLatestStreak.toString()
                    )
                }
            } else {
                if (mStreak < mTotalStreak) {
                    UserStreak(
                        userName = userName,
                        displayPic = displayPic,
                        streak = mStreak + 1,
                        highestStreak = mTotalStreak,
                        latestDate = date.toString()
                    )
                } else {
                    UserStreak(
                        userName = userName,
                        displayPic = displayPic,
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

    fun init() {
        val img: Int = resources.getIdentifier(selectedTheme + "_theme", "drawable", context?.packageName)
        binding.progressBar.progressDrawable = getDrawable(requireContext(), img)

        val balloon = Balloon.Builder(requireContext())
            .setArrowSize(10)
            .setText(requireContext().getString(R.string.tooltip_home))
            .setTextSize(15f)
            .setTextGravity(Gravity.START)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setPadding(6)
            .setMarginRight(8)
            .setCornerRadius(0f)
            .setOverlayShape(BalloonOverlayRect)
            .setBackgroundColor(requireContext().getColor(R.color.col_def))
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .setAutoDismissDuration(3000L)
            .build()

        binding.srlHome.setOnRefreshListener {
            presenter.getWaterData(today = date.toString())
        }

        binding.btnTooltip.setOnClickListener {
            balloon.showAlignBottom(binding.btnTooltip)
        }

        binding.btnChangeProgressBar.setOnClickListener {
            val fragment = SelectThemeFragment.getInstance(callback = this@HomeFragment)
            fragment.show(childFragmentManager, SelectThemeFragment.TAG)
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
                        presenter.addPoints(pointGot = waterAmt)
                        checkLevel()
                    }
                    override fun onNegative() {
                    }
                }
            )
        }
    }

    fun checkLevel() : Int {
        val expNeeded = presenter.checkExp(level)
        if(totalExp >= expNeeded) {
            level = level++
            presenter.levelUp()
            DialogUtil(requireActivity()).levelUp()
            return level
        }
        return level
    }
}