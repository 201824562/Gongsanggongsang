package com.example.userapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import com.example.userapp.ui.base.*
import com.example.userapp.databinding.ActivityMainBinding
import com.example.userapp.service.ReservationAlarmReceiver
import com.example.userapp.service.ReservationBeforeUseAlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override lateinit var  viewbinding: ActivityMainBinding
    override val viewmodel: MainActivityViewModel by viewModels()
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController : NavController
    var selectedItems : ArrayList<String> = arrayListOf()
    override fun initToolbar() {
        window.apply {
            navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.white)
        }
    }

    override fun initViewbinding() {
        viewbinding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewbinding.root)
    }

    override fun initViewStart(savedInstanceState: Bundle?) { }

    override fun initDataBinding(savedInstanceState: Bundle?) { }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        setToolbarWithNavcontroller()
    }


    //-----[네비에 따라 조절되는 기능 흐름 함수들]----------------------------------------------------------------------------------------------------------------------
    private fun setToolbarWithNavcontroller(){

        setSupportActionBar(viewbinding.toolbar)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            hideKeyboard()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            viewbinding.toolbar.setNavigationOnClickListener{ findNavController(R.id.nav_host).navigateUp() }   //이거 필요한가?
            when (destination.id){
                R.id.signInFragment, R.id.signInFindInfoFragment,
                R.id.signUpPermissionFragment, R.id.signUpAgencyFragment, R.id.signUpFirstFragment, R.id.signUpSecondFragment,
                R.id.reservationFacilitySelectFragment,
                R.id.settingsChangePwdFragment, R.id.settingsChangeInfoDetailFragment -> showToolbarTitle("")
                R.id.settingsChangeInfoFragment -> showToolbarTitle("계정 정보 관리")
                else -> hideToolbar()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.layout.fragment_main -> {
                finish()
                return true }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = this.currentFocus   // Check if no view has focus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS) } }

    private fun showToolbarTitle( title : String){
        viewbinding.toolbar.visibility = View.VISIBLE
        viewbinding.toolbarText.text = title
        supportActionBar?.show()
    }

    private fun hideToolbar(){
        viewbinding.toolbar.visibility = View.GONE
    }

    fun restartActivity() {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun getPhoto() : ArrayList<String>{
        return selectedItems
    }

    // reservation alarm manager
    fun setUseCompleteAlarm(endTimeCal :Calendar,click: Boolean, notificationId:Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, ReservationAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, notificationId, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        if(!click){
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                endTimeCal.timeInMillis,
                pendingIntent
            )
        }else{
            alarmManager.set(   // 5
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                0,
                pendingIntent
            )
        }

    }

    fun setBeforeUseAlarm(startTimeCal :Calendar, notificationId:Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, ReservationBeforeUseAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, notificationId, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        startTimeCal.add(Calendar.MINUTE,-5)
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA)
        Log.e("startTimeCal", sdf.format(startTimeCal.getTime()).toString())
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            startTimeCal.timeInMillis,
            pendingIntent
        )
    }
    fun setCancelUseAlarm(notificationId: Int){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, ReservationAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, notificationId, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

    fun setCancelBeforeUseAlarm(notificationId: Int){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, ReservationBeforeUseAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, notificationId, alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }
}

fun <VB : ViewBinding, VM : BaseSessionViewModel> BaseSessionFragment<VB, VM>.restartActivity() {
    val activity = this.activity as MainActivity
    activity.restartActivity()
}