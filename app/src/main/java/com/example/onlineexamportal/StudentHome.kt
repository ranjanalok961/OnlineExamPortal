package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class StudentHome : AppCompatActivity() {

    private lateinit var bottomNav : BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_home)
        setFragment(StudentDashboard())
        bottomNav = findViewById(R.id.studentBottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId ){
                R.id.studentMenuDashboard ->setFragment(StudentDashboard())
                R.id.studentMenuBook -> setFragment(StudentBook())
                R.id.studentMenuSetting -> setFragment(StudentSetting())
                else ->setFragment(StudentDashboard())
            }
            true
        }
    }
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.studentHomeFrame, fragment)
            .commit()
    }
}