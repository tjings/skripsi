package umn.ac.id.skripsijosh.ui.main

import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityMainBinding
import umn.ac.id.skripsijosh.pojo.CheckNotification
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.ui.notification.*
import umn.ac.id.skripsijosh.ui.notification.Notification
import umn.ac.id.skripsijosh.ui.register.biodata.BiodataActivity
import umn.ac.id.skripsijosh.utils.Util
import java.time.LocalDateTime
import java.util.*

class MainActivity : BaseActivity(), MainView, BottomNavigationView.OnNavigationItemSelectedListener {
    private val startDestinations = mapOf(
        R.id.menuHome to R.id.homeFragment,
        R.id.menuMedals to R.id.medalsFragment,
        R.id.menuProfile to R.id.profileFragment,
        R.id.menuLeaderboard to R.id.leaderboardFragment
    )
    private lateinit var binding : ActivityMainBinding
    private lateinit var presenter: MainPresenter
    private lateinit var homeTabContainer : View
    private lateinit var medalTabContainer : View
    private lateinit var shopTabContainer : View
    private lateinit var profileTabContainer : View
    private var notificationManager: NotificationManager? = null
    private var alarmManager: AlarmManager? = null
    private var isBiodataDone = false
    private var hourOfDay: Int = 0
    private var minutes: Int = 0

    private val timer = Timer()
    private val task: TimerTask = object : TimerTask() {
        override fun run() {
            checkNotif(fromTimer = true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = MainPresenter(this)
        presenter.checkBiodataDone()
        if(isBiodataDone) {
            init()
        }
        checkNotif()
        timer.schedule(task, 0L, 1000 * 60)
    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuHome -> {
                Navigation.findNavController(this, R.id.homeTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuHome))
                    }
                }
                makeOtherInvisible(homeTabContainer)
                return true
            }
            R.id.menuMedals -> {
                Navigation.findNavController(this, R.id.medalsTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuMedals))
                    }
                }
                makeOtherInvisible(medalTabContainer)
                return true
            }
            R.id.menuLeaderboard -> {
                Navigation.findNavController(this, R.id.shopTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuLeaderboard))
                    }
                }
                makeOtherInvisible(shopTabContainer)
                return true
            }
            R.id.menuProfile -> {
                Navigation.findNavController(this, R.id.profileTab).apply {
                    graph = navInflater.inflate(R.navigation.navi_graph).apply {
                        setStartDestination(startDestinations.getValue(R.id.menuProfile))
                    }
                }
                makeOtherInvisible(profileTabContainer)
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun makeOtherInvisible(container : View) {
        homeTabContainer.visibility = View.INVISIBLE
        medalTabContainer.visibility = View.INVISIBLE
        profileTabContainer.visibility = View.INVISIBLE
        shopTabContainer.visibility = View.INVISIBLE
        container.visibility = View.VISIBLE
    }

    override fun onGetDataUserSucces(userData: UserData) {
        val editor = sharedPreferences.edit()
        editor.putString("display_name", userData.displayName)
        editor.putString("gender", userData.gender)
        editor.putString("bday", userData.bday)
        editor.putString("weight", userData.weight)
        editor.putString("height", userData.height)
        editor.putString("display_pic", userData.displayPic)
        editor.putString("is_biodata_done", userData.isBiodataDone.toString())
        editor.apply()

        if(userData.isBiodataDone == false) {
            startActivity(Intent(this, BiodataActivity::class.java))
            finish()
        } else {
            init()
        }
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

    private fun init() {
        homeTabContainer = binding.homeTab
        medalTabContainer = binding.medalsTab
        profileTabContainer = binding.profileTab
        shopTabContainer = binding.shopTab
        binding.bottomNavigation.setOnItemSelectedListener(this)
    }


    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "Notif desc"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.createNotificationChannel(channel)
    }

    private fun setAlarms(fromTimer: Boolean = false) {
        val wakeHour = sharedPreferences.getString("wake_hour", "").toString()
        val wakeMins = sharedPreferences.getString("wake_minute", "").toString()
        val sleepHour = sharedPreferences.getString("sleep_hour", "").toString()
        val sleepMins = sharedPreferences.getString("sleep_minute", "").toString()
        val intent = Intent(this, Notification::class.java)

        if (Util.isNotNull(wakeHour) && Util.isNotNull(wakeMins)) {
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                11,
                intent,
                FLAG_UPDATE_CURRENT
            )
            intent.putExtra(titleExtra, "Its time to drink")
            intent.putExtra(
                messageExtra,
                "Good morning, start your day with some two cup of water!"
            )
            val time = setTime(hourOfDay = wakeHour.toInt(), minutes = wakeMins.toInt())
            alarmManager!!.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
        if (Util.isNotNull(sleepHour) && Util.isNotNull(sleepMins)) {
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                12,
                intent,
                FLAG_UPDATE_CURRENT
            )
            intent.putExtra(titleExtra, "Its time to drink")
            intent.putExtra(
                messageExtra,
                "Good night, drink some water to keep you hydrated throughout your sleep!"
            )
            val time = setTime(hourOfDay = sleepHour.toInt() - 1, minutes = sleepMins.toInt())
            alarmManager!!.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }

    private fun setReminder() {
        val intent = Intent(this, Notification::class.java)
        val sleepHour = sharedPreferences.getString("sleep_hour", "0")?.toInt()!!
        val progress = sharedPreferences.getString("progress", "0")?.toInt()!!
        hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        minutes = Calendar.getInstance().get(Calendar.MINUTE)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if(progress < 50) {
            if (hourOfDay in 11..15) {
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    13,
                    intent,
                    FLAG_UPDATE_CURRENT
                )
                intent.putExtra(titleExtra, "Its time to drink")
                intent.putExtra(messageExtra, "Looks like your body still far from today's share of water")
                val time = setTime(hourOfDay = hourOfDay, minutes = minutes + 5)
                alarmManager!!.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        }
        if(progress in 50..99) {
            if (hourOfDay in 16..sleepHour) {
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    13,
                    intent,
                    FLAG_UPDATE_CURRENT
                )
                intent.putExtra(titleExtra, "Its time to drink")
                intent.putExtra(messageExtra, "Don't forget to drink water, just some steps ahead!")
                val time = setTime(hourOfDay = hourOfDay, minutes = minutes + 5)
                alarmManager!!.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        }
    }

    private fun setTime(hourOfDay: Int = 12, minutes: Int = 30): Long {
        val current = LocalDateTime.now()
        val calendar = Calendar.getInstance()
        calendar.set(current.year, current.monthValue-1, current.dayOfMonth, hourOfDay, minutes)
        Log.d("alarmszz", calendar.timeInMillis.toString())
        return calendar.timeInMillis
    }

    private fun checkNotif(fromTimer: Boolean = false) {
        if(fromTimer) {
            setReminder()
        }
        if(sharedPreferences.getBoolean("enable_notif", true)) {
            createNotificationChannel()
            setAlarms()
        } else {
            if(notificationManager != null) {
                notificationManager!!.cancelAll()
                timer.cancel()
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CheckNotification) {
        Log.d("notif", sharedPreferences.getBoolean("enable_notif", true).toString())
        checkNotif()
    }
}