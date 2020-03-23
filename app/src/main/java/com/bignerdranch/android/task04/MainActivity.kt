package com.bignerdranch.android.task04

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration;

    override fun onSupportNavigateUp(): Boolean =
        findNavController(this, R.id.nav_host_fragment).navigateUp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(nav_view, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

//        appBarConfiguration = AppBarConfiguration(navController.graph) //configure nav controller
//        setupNavigation(navController) //setup navigation
//        setupActionBar(navController, appBarConfiguration) // setup action bar
//        //hear for event changes

    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigation(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        //fragments load from here but how ?
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.habitViewPagerFragment, R.id.appInfoFragment),
            drawerLayout
        )
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val retValue = super.onCreateOptionsMenu(menu)
//        val navigationView = findViewById<NavigationView>(R.id.nav_view)
//        if (navigationView == null) {
//            //android needs to know what menu I need
//            menuInflater.inflate(R.menu.activity_main_drawer, menu)
//            return true
//        }
//        return retValue
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //I need to open the drawer onClick
        when (item!!.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)

            }
        }
        return item.onNavDestinationSelected(findNavController(this, R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //the code is beautiful enough without comments
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}