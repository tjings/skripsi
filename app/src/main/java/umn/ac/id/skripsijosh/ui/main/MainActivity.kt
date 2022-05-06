package umn.ac.id.skripsijosh.ui.main

import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.databinding.ActivityMainBinding
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.ui.register.biodata.BiodataActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.ui.notification.*
import umn.ac.id.skripsijosh.ui.notification.Notification
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
    private var isBiodataDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = MainPresenter(this)
        presenter.checkBiodataDone()
        Log.d(TAG, isBiodataDone.toString())
        if(sharedPreferences.getBoolean("enable_notif", true)) {
            createNotificationChannel()
        } else {
            if(notificationManager != null) {
                notificationManager!!.cancelAll()
            } else {
                return
            }
        }
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
                Log.d("error", "error on navigating tab")
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
        } else {
            isBiodataDone = true
            homeTabContainer = binding.homeTab
            medalTabContainer = binding.medalsTab
            profileTabContainer = binding.profileTab
            shopTabContainer = binding.shopTab
            binding.bottomNavigation.setOnItemSelectedListener(this)
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

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "Notif desc"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.createNotificationChannel(channel)

        //scheduler
        val intent = Intent(applicationContext, Notification::class.java)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val wakeHour = sharedPreferences.getString("wake_hour", "").toString()
        val wakeMins = sharedPreferences.getString("wake_minute", "").toString()
        val sleepHour = sharedPreferences.getString("sleep_hour", "").toString()
        val sleepMins = sharedPreferences.getString("sleep_minute", "").toString()

        if(Util.isNotNull(wakeHour) && Util.isNotNull(wakeMins)) {
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent. FLAG_UPDATE_CURRENT
            )
            val title = "Its time to drink!"
            val message = "Good morning, start your day with some two cup of water!"
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)
            val wakeTime = setTime(hourOfDay = wakeHour.toInt(), minutes = wakeMins.toInt())
            Log.d("wake", wakeTime.toString())
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                wakeTime,
                pendingIntent
            )
        }
        if(Util.isNotNull(sleepHour) && Util.isNotNull(sleepMins)) {
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent. FLAG_UPDATE_CURRENT
            )
            val title = "Its time to drink!"
            val message = "Good night, drink some water to keep you hydrated throughout your sleep!"
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)
            val sleepTime = setTime(hourOfDay = sleepHour.toInt() - 1, minutes = sleepMins.toInt())
            Log.d("sleep", sleepTime.toString())
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                sleepTime,
                pendingIntent
            )
        }
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent. FLAG_UPDATE_CURRENT
        )
        val title = "Its time to drink!"
        val message = "Ugh, your body feels thirsty. Go drink some water, and check in!"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val time = setTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun setTime(hourOfDay: Int = 16, minutes: Int = 30): Long {
        val current = LocalDateTime.now()
        val calendar = Calendar.getInstance()
        calendar.set(current.year, current.monthValue-1, current.dayOfMonth, hourOfDay, minutes)
        return calendar.timeInMillis
    }
}