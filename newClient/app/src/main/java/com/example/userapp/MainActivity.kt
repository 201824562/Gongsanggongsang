package com.example.userapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import com.example.userapp.base.*
import com.example.userapp.data.dto.UserModel
import com.example.userapp.databinding.ActivityMainBinding
import kotlinx.coroutines.channels.Channel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {
    companion object{ val TOOLBAR_TITLE = "title" }

    override lateinit var  viewbinding: ActivityMainBinding
    override val viewmodel: MainActivityViewModel by viewModels()
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController : NavController
    private var userData : UserModel? = null
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

    override fun initViewStart(savedInstanceState: Bundle?) {}

    override fun initDataBinding(savedInstanceState: Bundle?) {
        viewmodel.onSuccessGettingUserInfo.observe(this, {  userData = it })
        viewmodel.onSuccessGettingNullUserInfo.observe(this, {
            //restartActivity()
        })
    }

    override fun initViewFinal(savedInstanceState: Bundle?) {
        viewmodel.getUserInfo()
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
    fun getUserData() : UserModel{
        return this.userData!!
    }

//    fun displayNotification(){
//        val notificationId = 45
//        val notification: Notification = NotificationCompat.Builder(this, ChannelID)
//            .setSmallIcon(R.drawable.ic_box)
//            .setContentTitle("textTitle")
//            .setContentText("textContent")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .build()
//
////        val notificationManager : NotificationManager
//        notificationManager?.notify()
//    }

    fun createNotificationChannel(id :String, name :String) : NotificationCompat.Builder{

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)

            manager.createNotificationChannel(channel)

            NotificationCompat.Builder(this, id)

        } else {
            NotificationCompat.Builder(this)
        }
    }

//    private fun createNotificationChannel(CHANNEL_ID:String, channel_name:String, channel_description:String) {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//
//            val channel = NotificationChannel(CHANNEL_ID, channel_name, importance).apply {
//                description = channel_description
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
}

fun <VB : ViewBinding, VM : BaseSessionViewModel> BaseSessionFragment<VB, VM>.restartActivity() {
    val activity = this.activity as MainActivity
    activity.restartActivity()
}