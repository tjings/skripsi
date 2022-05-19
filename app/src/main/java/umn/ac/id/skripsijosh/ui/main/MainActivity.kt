package umn.ac.id.skripsijosh.ui.main

import android.app.*
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityMainBinding
import umn.ac.id.skripsijosh.pojo.CheckNotification
import umn.ac.id.skripsijosh.pojo.Logout
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.ui.notification.*
import umn.ac.id.skripsijosh.ui.notification.Notification
import umn.ac.id.skripsijosh.ui.register.biodata.BiodataActivity
import umn.ac.id.skripsijosh.ui.welcome.WelcomeActivity
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
    private var timer: Timer? = null
    private var alarmsCreated = false
    private var backButtonCount = 0

    private val task: TimerTask = object : TimerTask() {
        override fun run() {
            if(sharedPreferences.getBoolean("enable_notif", true)) {
                if (notificationManager == null) {
                    createNotificationChannel()
                }
                setReminder()
            } else {
                if(notificationManager != null) {
                    notificationManager!!.cancelAll()
                }
            }
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
        timer = Timer().apply {
            //check per 2 jam
            schedule(task, 1000L, 1000 * 60 * 60 * 2)
        }
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
        editor.putInt("level", userData.level!!)
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

    override fun onBackPressed() {
        if (backButtonCount >= 1) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            backButtonCount++
            Toast.makeText(
                this,
                "Press the back button once again to close the application.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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

    private fun setAlarms() {
        val wakeHour = sharedPreferences.getString("wake_hour", "").toString()
        val wakeMins = sharedPreferences.getString("wake_minute", "").toString()
        val sleepHour = sharedPreferences.getString("sleep_hour", "").toString()
        val sleepMins = sharedPreferences.getString("sleep_minute", "").toString()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Util.isNotNull(wakeHour) && Util.isNotNull(wakeMins)) {
            val intent = Intent(this, Notification::class.java)
            intent.putExtra(notificationID, "0")
            intent.putExtra(titleExtra, "Its time to drink")
            intent.putExtra(messageExtra, "Good morning, start your day with some cup of water!")
            intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            Log.d("from", "morning")
            val thuReq: Long = Calendar.getInstance().timeInMillis + 1
            val reqReqCode = thuReq.toInt()
            val pendingIntent = getBroadcast(
                this,
                reqReqCode,
                intent,
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
            val time = setTime(hourOfDay = wakeHour.toInt(), minutes = wakeMins.toInt())
            alarmManager!!.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
        if (Util.isNotNull(sleepHour) && Util.isNotNull(sleepMins)) {
            val intent = Intent(this, Notification::class.java)
            intent.putExtra(notificationID, "1")
            intent.putExtra(titleExtra, "Its time to drink")
            intent.putExtra(messageExtra, "Good night, drink some water to keep you hydrated throughout your sleep!")
            intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            Log.d("from", "nite")
            val thuReq: Long = Calendar.getInstance().timeInMillis + 1
            val reqReqCode = thuReq.toInt()
            val pendingIntent = getBroadcast(
                this,
                reqReqCode,
                intent,
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )
            val time = setTime(hourOfDay = sleepHour.toInt() - 1, minutes = sleepMins.toInt())
            alarmManager!!.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
        alarmsCreated = true
    }

    private fun setReminder() {
        val progress = sharedPreferences.getString("progress", "0")?.toInt()!!
        hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        minutes = Calendar.getInstance().get(Calendar.MINUTE)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val wakeHour = sharedPreferences.getString("wake_hour", "8")!!.toInt()
        val sleepHour = sharedPreferences.getString("sleep_hour", "23")!!.toInt()

        if(progress < 50) {
            if (hourOfDay in wakeHour..sleepHour) {
                val intent = Intent(this, Notification::class.java)
                intent.putExtra(notificationID, "2")
                intent.putExtra(titleExtra, "Its time to drink")
                intent.putExtra(messageExtra, "Looks like your body still far from today's share of water")
                intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                Log.d("from", "morning1")
                val thuReq: Long = Calendar.getInstance().timeInMillis + 1
                val reqReqCode = thuReq.toInt()
                val pendingIntent = getBroadcast(
                    this,
                    reqReqCode,
                    intent,
                    FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )
                val time = setTime(hourOfDay = hourOfDay, minutes = minutes)
                alarmManager!!.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        }
        if(progress in 50..99) {
            if (hourOfDay in 18..sleepHour) {
                val intent = Intent(this, Notification::class.java)
                intent.putExtra(notificationID, "3")
                intent.putExtra(titleExtra, "Its time to drink")
                intent.putExtra(messageExtra, "Don't forget to drink water, just some steps ahead!")
                intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                Log.d("from", "morning2")
                val thuReq: Long = Calendar.getInstance().timeInMillis + 1
                val reqReqCode = thuReq.toInt()
                val pendingIntent = getBroadcast(
                    this,
                    reqReqCode,
                    intent,
                    FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )
                val time = setTime(hourOfDay = hourOfDay, minutes = minutes)
                alarmManager!!.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        }
    }

    private fun setTime(hourOfDay: Int, minutes: Int): Long {
        val current = LocalDateTime.now()
        val calendar = Calendar.getInstance()
        calendar.set(current.year, current.monthValue-1, current.dayOfMonth, hourOfDay, minutes)
        Log.d("alarmszz", calendar.timeInMillis.toString())
        return calendar.timeInMillis
    }

    private fun checkNotif() {
        if(sharedPreferences.getBoolean("enable_notif", true)) {
            createNotificationChannel()
            setAlarms()
        } else {
            if(notificationManager != null) {
                notificationManager!!.cancelAll()
                timer?.cancel()
                alarmsCreated = false
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CheckNotification) {
        Log.d("notif", sharedPreferences.getBoolean("enable_notif", true).toString())
        checkNotif()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: Logout) {
        auth.signOut()
        sharedPreferences.edit().clear().apply()
        if(notificationManager != null) {
            notificationManager!!.cancelAll()
            timer?.cancel()
            alarmsCreated = false
        }
        finish()
        startActivity(Intent(this, WelcomeActivity::class.java))
    }
}