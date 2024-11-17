package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfessorHome : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_professor_home)
        bottomNav = findViewById(R.id.professorBottomNav)

        setFragment(professorDashboardFrag())
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId ){
                R.id.ProfessorMenuDashboard ->setFragment(professorDashboardFrag())
                R.id.ProfessorMenuSetting -> setFragment(professorSettingFrag())
                else ->setFragment(professorDashboardFrag())
            }
            true
        }
    }
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.professorFrame, fragment)
            .commit()
    }
}