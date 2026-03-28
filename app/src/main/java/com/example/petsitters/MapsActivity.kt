package com.example.petsitters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent

data class PetSitter(val name: String, val latLng: LatLng, val animalType: String, val bio: String)

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pet Sitter Map"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<FloatingActionButton>(R.id.fab_back).setOnClickListener { finish() }
        val fabBack = findViewById<FloatingActionButton>(R.id.fab_back)
        fabBack.setOnClickListener {
            finish() // Simply closes the map and goes back to Home
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val filter = intent.getStringExtra("FILTER_TYPE")

        val norfolkCenter = LatLng(36.8508, -76.2859)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(norfolkCenter, 12f))

        val sitters = listOf(
            PetSitter("Alex", LatLng(36.8508, -76.2859), "Dog", "Great with Huskies"),
            PetSitter("Sam", LatLng(36.8650, -76.2950), "Reptile", "Expert in Bearded Dragons"),
            PetSitter("Jordan", LatLng(36.8450, -76.2750), "Bird", "Parrot specialist"),
            PetSitter("Casey", LatLng(36.8600, -76.2800), "Cat", "Quiet home")
        )

        for (sitter in sitters) {
            if (filter == null || sitter.animalType == filter) {
                mMap.addMarker(MarkerOptions().position(sitter.latLng).title("${sitter.name} - ${sitter.animalType} Sitter").snippet(sitter.bio))
            }
        }

        val sharedPref = getSharedPreferences("SitterPrefs", android.content.Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("name", null)
        val savedType = sharedPref.getString("type", null)

        if (savedName != null) {
            val savedLat = sharedPref.getFloat("lat", 0f).toDouble()
            val savedLng = sharedPref.getFloat("lng", 0f).toDouble()
            val savedBio = sharedPref.getString("bio", "New Sitter")

            // FIX: Use ?. and == true to safely check the filter
            if (filter == null || (savedType?.contains(filter) == true)) {
                val newLoc = LatLng(savedLat, savedLng)
                mMap.addMarker(MarkerOptions().position(newLoc).title("$savedName ($savedType)").snippet(savedBio))
            }
        }
        mMap.setOnMarkerClickListener { marker ->
            // Create an intent to open the profile
            val intent = Intent(this, SitterProfileActivity::class.java)

            // SAFE CHECK: If title or snippet is null, use a default string to prevent crash
            val name = marker.title ?: "Unknown Sitter"
            val bio = marker.snippet ?: "No bio available"

            intent.putExtra("SITTER_NAME", name)
            intent.putExtra("SITTER_BIO", bio)

            startActivity(intent)
            true
        }
    }
}