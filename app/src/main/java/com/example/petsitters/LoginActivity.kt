package com.example.petsitters

import android.content.Intent
import android.widget.TextView
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // This shows the "<-" arrow in the top left
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create Account"

        // 1. Regular Login Button
        val loginButton = findViewById<Button>(R.id.btn_login)
        val emailInput = findViewById<EditText>(R.id.et_login_email)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            if (email.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }

        // Inside LoginActivity onCreate
        val signUpText = findViewById<TextView>(R.id.tv_go_to_signup)

        signUpText.setOnClickListener {
            // This opens your registration page
            val intent = Intent(this, RegisterSitterActivity::class.java)
            startActivity(intent)
        }

        // Guest Login Button
        val guestButton = findViewById<Button>(R.id.btn_guest_login)
        guestButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed() // This sends the user back to LoginActivity
        return true
    }
}
