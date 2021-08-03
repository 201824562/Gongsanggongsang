package com.example.userapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.userapp.base.*
import com.example.userapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {
    companion object{ val TOOLBAR_TITLE = "title" }

    override lateinit var  viewbinding: ActivityMainBinding
    override val viewmodel: MainActivityViewModel by viewModels()
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController : NavController

    //TODO : 메인액티비티를 사용하지 않고 정식으로 넘겨주는 걸로 바꿀것.
    var selected_items : ArrayList<String> = arrayListOf()

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

    override fun initDataBinding(savedInstanceState: Bundle?) {}

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
                R.id.introFragment, R.id.signUpWaitFragment -> hideToolbar()
                R.id.signInFragment, R.id.signUpPermissionFragment, R.id.signUpAgencyFragment,
                R.id.signUpFirstFragment, R.id.signUpSecondFragment, R.id.reservationFacilitySelectFragment-> showToolbarTitle("")
                R.id.splashFragment, R.id.mainFragment -> hideToolbar()
                else -> showToolbarTitle("각자프래그에 맞는 이름으로 추가해주기.")
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
    fun selectPhoto(select_photo_uri : String) {
        if(select_photo_uri in selected_items){
            selected_items.remove(select_photo_uri)
        }
        else{
            selected_items.add(select_photo_uri)
        }
        System.out.println(select_photo_uri)
    }
    fun getPhoto() : ArrayList<String>{
        return selected_items
    }
}

fun <VB : ViewBinding, VM : BaseSessionViewModel> BaseSessionFragment<VB, VM>.restartActivity() {
    val activity = this.activity as MainActivity
    activity.restartActivity()
}