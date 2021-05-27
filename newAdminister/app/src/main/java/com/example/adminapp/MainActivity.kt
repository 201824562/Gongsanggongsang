package com.example.adminapp

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.adminapp.base.BaseActivity
import com.example.adminapp.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {
    companion object{
        val TOOLBAR_TITLE = "title"
    }

    override lateinit var  viewbinding: ActivityMainBinding
    override val viewmodel: MainActivityViewModel by viewModel()
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController : NavController

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
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            hideKeyboard()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            viewbinding.toolbar.setNavigationOnClickListener{ findNavController(R.id.nav_host).navigateUp() }   //이거 필요한가?
            when (destination.id){
                R.id.introFragment -> hideToolbar()
                R.id.mainFragment -> hideToolbar()
                else -> showToolbarTitle("각자프래그에 맞는 이름으로 추가해주기.")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.layout.fragment_mainhome -> {
                finish()
                return true } }
        return super.onOptionsItemSelected(item)
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = this.currentFocus   // Check if no view has focus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS) } }

    private fun showToolbarTitle(title : String){
        viewbinding.toolbar.visibility = View.VISIBLE
        viewbinding.toolbarText.text = title
        supportActionBar?.show()
    }

    private fun hideToolbar(){
        viewbinding.toolbar.visibility = View.GONE
    }



}