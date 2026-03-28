package com.example.petsitters

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu

class MainActivity : AppCompatActivity() {

    private var selectedAnimal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searcher_home)

        val searchButton = findViewById<Button>(R.id.btn_search)
        val otherButton = findViewById<ImageButton>(R.id.btn_other)
        val dogButton = findViewById<ImageButton>(R.id.btn_dog)
        val catButton = findViewById<ImageButton>(R.id.btn_cat)
        val beSitterButton = findViewById<Button>(R.id.btn_be_sitter)

        dogButton.setOnClickListener {
            selectedAnimal = "Dog"
            searchButton.text = "Search for Dog Sitters"
        }

        catButton.setOnClickListener {
            selectedAnimal = "Cat"
            searchButton.text = "Search for Cat Sitters"
        }

        otherButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add("Reptile")
            popup.menu.add("Bird")
            popup.menu.add("Fish")
            popup.menu.add("Rodent")
            popup.menu.add("Rabbit")
            popup.menu.add("Arachnid")

            popup.setOnMenuItemClickListener { item ->
                selectedAnimal = item.title.toString()
                searchButton.text = "Search for $selectedAnimal Sitters"
                true
            }
            popup.show()
        }

        searchButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("FILTER_TYPE", selectedAnimal)
            startActivity(intent)
        }

        beSitterButton.setOnClickListener {
            startActivity(Intent(this, RegisterSitterActivity::class.java))
        }
    }
}