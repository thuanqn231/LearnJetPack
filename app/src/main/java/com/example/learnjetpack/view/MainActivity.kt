package com.example.learnjetpack.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.learnjetpack.R

class MainActivity : AppCompatActivity() {
    private  lateinit var navControler: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navControler = Navigation.findNavController(this, R.id.fragment2)
        NavigationUI.setupActionBarWithNavController(this,navControler)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navControler,null)
    }

}
