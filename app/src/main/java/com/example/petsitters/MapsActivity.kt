package com.example.petsitters

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

// UPDATED: Using org.osmdroid.util.GeoPoint instead of Google LatLng
data class PetSitter(val name: String, val latLng: GeoPoint, val animalType: String, val bio: String)

class MapsActivity : AppCompatActivity() {

    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // CRITICAL: Initialize OSM configuration before setContentView
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_maps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pet Sitter Map"

        // Setup the Map
        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(12.5)
        val norfolkCenter = GeoPoint(36.8508, -76.2859)
        mapController.setCenter(norfolkCenter)

        findViewById<FloatingActionButton>(R.id.fab_back).setOnClickListener { finish() }

        setupSitters()
    }

    private fun setupSitters() {
        val filter = intent.getStringExtra("FILTER_TYPE")

        val sitters = listOf(
            PetSitter("Alex", GeoPoint(36.8508, -76.2859), "Dog", "Great with Huskies"),
            PetSitter("Sam", GeoPoint(36.8650, -76.2950), "Reptile", "Expert in Bearded Dragons"),
            PetSitter("Jordan", GeoPoint(36.8450, -76.2750), "Bird", "Parrot specialist"),
            PetSitter("Casey", GeoPoint(36.8600, -76.2800), "Cat", "Quiet home")
        )

        // Add hardcoded sitters
        for (sitter in sitters) {
            if (filter == null || sitter.animalType == filter) {
                addSitterMarker(sitter.latLng, "${sitter.name} - ${sitter.animalType} Sitter", sitter.bio)
            }
        }

        // Add saved sitter from SharedPreferences
        val sharedPref = getSharedPreferences("SitterPrefs", MODE_PRIVATE)
        val savedName = sharedPref.getString("name", null)
        if (savedName != null) {
            val savedType = sharedPref.getString("type", "") ?: ""
            if (filter == null || savedType.contains(filter)) {
                val savedLat = sharedPref.getFloat("lat", 0f).toDouble()
                val savedLng = sharedPref.getFloat("lng", 0f).toDouble()
                val savedBio = sharedPref.getString("bio", "New Sitter") ?: ""
                addSitterMarker(GeoPoint(savedLat, savedLng), "$savedName ($savedType)", savedBio)
            }
        }
    }

    private fun addSitterMarker(point: GeoPoint, title: String, bio: String) {
        val marker = Marker(map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        marker.snippet = bio
        
        // Handle clicking the marker to open profile
        marker.setOnMarkerClickListener { m, _ ->
            val intent = Intent(this, SitterProfileActivity::class.java)
            intent.putExtra("SITTER_NAME", m.title)
            intent.putExtra("SITTER_BIO", m.snippet)
            startActivity(intent)
            true
        }
        
        map.overlays.add(marker)
    }

    override fun onResume() {
        super.onResume()
        map.onResume() 
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
