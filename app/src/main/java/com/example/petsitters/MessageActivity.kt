package com.example.petsitters

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        // 1. Get the Sitter's name from the Profile click
        val sitterName = intent.getStringExtra("SITTER_NAME")
        findViewById<TextView>(R.id.tv_chat_with).text = "Chat with $sitterName"

        // 2. Find the input and container
        val etMessage = findViewById<EditText>(R.id.et_message_input)
        val btnSend = findViewById<Button>(R.id.btn_send_message)
        val llChat = findViewById<LinearLayout>(R.id.ll_chat_container)

        // 3. Handle Send Button
        btnSend.setOnClickListener {
            val messageText = etMessage.text.toString()
            if (messageText.isNotEmpty()) {
                // Create a "Message Bubble"
                val newMessage = TextView(this)
                newMessage.text = "You: $messageText"
                newMessage.textSize = 16f
                newMessage.setPadding(20, 15, 20, 15)

                // Add it to the conversation list
                llChat.addView(newMessage)

                // Clear the box
                etMessage.text.clear()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed() // Modern way to go back
        return true
    }
}