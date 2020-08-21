package com.shan.mybookhub.activity.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.shan.mybookhub.*
import com.shan.mybookhub.activity.fragment.AboutFragment
import com.shan.mybookhub.activity.fragment.DashboardFragment
import com.shan.mybookhub.activity.fragment.FavouritesFragment
import com.shan.mybookhub.activity.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hooks
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()
        openDashboard()
         //to open and close the action bar
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        //to make the hamburger function for a click and to syn the <- button with hamburger
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        //to set click listener for the menu buttons
       navigationView.setNavigationItemSelectedListener {
            // to make the frags highlighted
            if(previousMenuItem !=null){
                previousMenuItem?.isChecked=false
                it.isChecked=true
            }
            it.isCheckable=true
            previousMenuItem=it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportActionBar?.title = "Favourites"
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouritesFragment()
                        )
                        // .addToBackStack("favourites") to store it in container to get it back
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportActionBar?.title = "Profile"
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        )
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.about -> {
                    supportActionBar?.title = "About"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment()
                        ).commit()
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true //lambas
        }
    }
    //to set toolbar
    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "ActionBar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    //to open the drawer on clicking hambuger (gravity)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    //to open the dashboard frag by default
    fun openDashboard() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        supportActionBar?.title = "Dashboard"
        transaction.replace(R.id.frameLayout, DashboardFragment())
        transaction.commit()
        navigationView.setCheckedItem(R.id.dashboard)
    }
// backstack can be removed by using this
    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when (frag) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
}




