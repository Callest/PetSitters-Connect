package com.example.petsitters

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class RegisterSitterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_sitter)

        // --- Navigation ---
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Register as Sitter"

        // --- UI Elements ---
        val submitButton = findViewById<Button>(R.id.btn_submit_registration)
        val nameInput = findViewById<EditText>(R.id.et_sitter_name)
        val addressInput = findViewById<EditText>(R.id.et_sitter_address)
        val bioInput = findViewById<EditText>(R.id.et_sitter_bio)

        submitButton.setOnClickListener {
            val name = nameInput.text.toString()
            val rawAddress = addressInput.text.toString()
            val bio = bioInput.text.toString()

            val selectedPets = mutableListOf<String>()
            if (findViewById<CheckBox>(R.id.cb_reg_dog).isChecked) selectedPets.add("Dog")
            if (findViewById<CheckBox>(R.id.cb_reg_cat).isChecked) selectedPets.add("Cat")
            if (findViewById<CheckBox>(R.id.cb_reg_bird).isChecked) selectedPets.add("Bird")
            if (findViewById<CheckBox>(R.id.cb_reg_reptile).isChecked) selectedPets.add("Reptile")
            if (findViewById<CheckBox>(R.id.cb_reg_fish).isChecked) selectedPets.add("Fish")
            if (findViewById<CheckBox>(R.id.cb_reg_rodent).isChecked) selectedPets.add("Rodent")
            if (findViewById<CheckBox>(R.id.cb_reg_rabbit).isChecked) selectedPets.add("Rabbit")
            if (findViewById<CheckBox>(R.id.cb_reg_arachnid).isChecked) selectedPets.add("Arachnid")

            val finalTypes = selectedPets.joinToString(", ")

            if (name.isNotEmpty() && rawAddress.isNotEmpty()) {
                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    // Force Norfolk, VA to keep the demo local
                    val addresses = geocoder.getFromLocationName("$rawAddress, Norfolk, VA", 1)

                    // Change your current address block to this:
                    if (addresses != null && addresses.isNotEmpty()) {
                        // Adding the [0] correctly grabs the first location found
                        val lat = addresses[0].latitude
                        val lng = addresses[0].longitude

                        val sharedPref = getSharedPreferences("SitterPrefs", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("name", name)
                            putString("bio", bio)
                            putString("type", finalTypes)
                            putFloat("lat", lat.toFloat())
                            putFloat("lng", lng.toFloat())
                            apply()
                        }
                        Toast.makeText(this, "Registered for: $finalTypes", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(this, "Address not found in Norfolk.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This handles the Back Arrow click in the top bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}