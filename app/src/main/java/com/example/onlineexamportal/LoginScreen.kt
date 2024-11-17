package com.example.onlineexamportal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginScreen : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var googleLoginButton: Button

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_screen, container, false)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)
        signupButton = view.findViewById(R.id.signupButton)
        googleLoginButton = view.findViewById(R.id.googleLoginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            var userId = user?.uid
                            userId?.let {
                                firestore.collection("users").document(userId)
                                    .get()
                                    .addOnSuccessListener { data ->
                                        val userRole = data.getString("role")
                                        Toast.makeText(activity, "Role $userRole", Toast.LENGTH_SHORT).show()
                                        if (userRole == "Student") {
                                            startActivity(Intent(requireActivity(), StudentHome::class.java))
                                        } else if (userRole == "Professor") {
                                            startActivity(Intent(requireActivity(), ProfessorHome::class.java))
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore", "Data fetch unsuccessful: ${e.message}")
                                    }
                            }
                            Toast.makeText(activity, "Login SuccessFul.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(activity, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignUpScreen())
                .addToBackStack(null)
                .commit()
        }

        googleLoginButton.setOnClickListener {
            // Handle Google login logic
            Toast.makeText(activity, "Google Login clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
