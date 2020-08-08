package com.kierdavis.fuchsia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.navigation_items, R.id.navigation_collections
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        setupNavigation()
    }

    private fun setupNavigation() {
        findNavController(R.id.nav_host_fragment).let { navController ->
            setupActionBarWithNavController(navController, appBarConfiguration)
            findViewById<BottomNavigationView>(R.id.nav_view).setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
