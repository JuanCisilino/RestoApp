package com.starlabs.restoapp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.starlabs.restoapp.R
import com.starlabs.restoapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    companion object {
        fun start(activity: Activity){
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onBackPressed() {
        Toast.makeText(this, "signed out", Toast.LENGTH_LONG).show()
        viewModel.signOut(this)
        super.onBackPressed()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setPrefs(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration =
            when (viewModel.getRol()) {
                "admin" -> { AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_admin)) }
                else -> { AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_profile)) }
            }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}