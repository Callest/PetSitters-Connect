package com.example.petsitters

import android.os.Bundle
import android.widget.Button        // ADDED
import android.widget.EditText      // ADDED
import android.widget.LinearLayout  // ADDED
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class SitterProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitter_profile)

        // 1. "Catch" the data from the Map click
        val name = intent.getStringExtra("SITTER_NAME")
        val bio = intent.getStringExtra("SITTER_BIO")

        // 2. Find the TextBoxes from your XML
        val tvName = findViewById<TextView>(R.id.tv_profile_name)
        val tvBio = findViewById<TextView>(R.id.tv_profile_bio)

        // 3. Put the data on the screen
        tvName.text = name
        tvBio.text = bio

        val btnMessage = findViewById<Button>(R.id.btn_message_sitter)
        btnMessage.setOnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            // Pass the sitter's name to the chat screen
            intent.putExtra("SITTER_NAME", name)
            startActivity(intent)
        }

        // 4. Handle the Review/Comment Logic
        val btnPost = findViewById<Button>(R.id.btn_post_comment)
        val etComment = findViewById<EditText>(R.id.et_new_comment)
        val llComments = findViewById<LinearLayout>(R.id.ll_comments_container)

        btnPost.setOnClickListener {
            val commentText = etComment.text.toString()
            if (commentText.isNotEmpty()) {
                // Create a new TextView for the comment
                val newReview = TextView(this)
                newReview.text = "⭐ $commentText"
                newReview.textSize = 16f
                newReview.setPadding(0, 10, 0, 10)

                // Add it to the list
                llComments.addView(newReview)

                // Clear the box
                etComment.text.clear()
            }
        }
// 1. Show the arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

// 2. Add a Title (Optional but looks professional)
        supportActionBar?.title = "Sitter Profile" // Or "Register"
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed() // Modern way to go back
        return true
    }
}