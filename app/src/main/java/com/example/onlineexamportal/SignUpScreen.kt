package com.example.onlineexamportal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpScreen : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var loginButton: Button
    private lateinit var signupWithEmailButton: Button

    private lateinit var userTypeRadioGroup: RadioGroup
    private lateinit var professorRadioButton: RadioButton
    private lateinit var studentRadioButton: RadioButton

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_screen, container, false)

        nameEditText = view.findViewById(R.id.nameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        signupButton = view.findViewById(R.id.signupButton)
        loginButton = view.findViewById(R.id.loginButton)
        signupWithEmailButton = view.findViewById(R.id.signupWithEmailButton)

        userTypeRadioGroup = view.findViewById(R.id.userTypeRadioGroup)
        professorRadioButton = view.findViewById(R.id.radioProfessor)
        studentRadioButton = view.findViewById(R.id.radioStudent)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            val userType = when (userTypeRadioGroup.checkedRadioButtonId) {
                R.id.radioProfessor -> "Professor"
                R.id.radioStudent -> "Student"
                else -> ""
            }

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || userType.isEmpty()) {
                Toast.makeText(activity, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(activity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { signUpTask ->
                    if (signUpTask.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid
                        val userData = mapOf(
                            "name" to name,
                            "role" to userType,
                            "email" to email
                        )

                        userId?.let {
                            firestore.collection("users").document(it)
                                .set(userData)
                                .addOnSuccessListener {
                                    Log.d("Firestore", "User details successfully stored in Firestore.")
                                    Toast.makeText(activity, "Signup successful as $userType", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "Error storing user details: ${e.message}")
                                }
                        }
                    } else {
                        Log.e("FirebaseAuth", "Sign up failed: ${signUpTask.exception?.message}")
                        Toast.makeText(activity, "Sign up failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginButton.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, LoginScreen())
                ?.addToBackStack(null)
                ?.commit()
        }

        signupWithEmailButton.setOnClickListener {
            Toast.makeText(activity, "Signup with Email clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
