package com.example.onlineexamportal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Log.d("FirebaseAuth", "User is logged in: ${currentUser.email}")
            var userId = currentUser.uid
            userId.let {
                firestore.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener { data ->
                        val userRole = data.getString("role")
                        if (userRole == "Student") {
                            startActivity(Intent(this, StudentHome::class.java))
                        } else if (userRole == "Professor") {
                            startActivity(Intent(this, ProfessorHome::class.java))
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Data fetch unsuccessful: ${e.message}")
                    }
            }
        } else {
            Log.d("FirebaseAuth", "No user is logged in")
            setFragment(LoginScreen())
        }
    }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}