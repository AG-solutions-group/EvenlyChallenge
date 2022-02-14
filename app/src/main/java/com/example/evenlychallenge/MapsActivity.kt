package com.example.evenlychallenge

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.evenlychallenge.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        // global variables to pass data to fragment
        // TODO alternative: maybe it is best practice for bigger projects to pass data directly to fragments (or in some other way?)
        var clickedVenueID = ""
        lateinit var mMap: GoogleMap
        var lastClickedVenueMarker : Marker? = null
        var markerList = mutableListOf<Marker>()
    }

    lateinit var binding: ActivityMapsBinding
    private val fragmentPlacesOverview = PlacesOverviewFragment()
    private val fragmentPlacesDetails = PlacesDetailsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // show fragment to show an overview of all places
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragmentPlacesOverview)
                .addToBackStack(null)
                .commit()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onBackPressed() {
        // super.onBackPressed()
        // only do something if details fragment is showing -> show overview fragment
        if (supportFragmentManager.findFragmentById(R.id.fragment) == fragmentPlacesDetails) {
            // reset all clicked places
            lastClickedVenueMarker = null
            clickedVenueID = ""

            // remove all markers and change fragment
            // alternative: dont remove markers and dont reload data in overview fragment, just load fragment and work with old data
            markerList.clear()
            mMap.clear()

            // show overview fragment
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragmentPlacesOverview)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride") // suppress -> google maps addon interferes with original google maps -> no problems in this use case
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // disable some POI on google map so it doesnt distract
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

        // do things on click on marker
        mMap.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener() {
            changeMarker(it)
            true
        })
    }

    fun changeMarker(it: Marker) {
        if (it.title != "Evenly HQ") {
            // change marker icon of last icon && clicked icon
            if (lastClickedVenueMarker != null) lastClickedVenueMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(makeIcon(
                Color.parseColor("#FDC26C"), markerList.indexOf(lastClickedVenueMarker) + 1)))
            it.setIcon(BitmapDescriptorFactory.fromBitmap(makeIcon(Color.parseColor("#FFFF6F00"), markerList.indexOf(it) + 1)))
            // after changing icon remember current marker for next click
            lastClickedVenueMarker = it

            // details fragment
            clickedVenueID = it.tag.toString() // pass id via global var -> not sure if this is best practice
            if (supportFragmentManager.findFragmentById(R.id.fragment) != fragmentPlacesDetails) changeFragment()
            else fragmentPlacesDetails.loadDetails()
        }
    }

    fun changeFragment(){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragmentPlacesDetails)
                .addToBackStack(null)
                .commit()
        }
    }

    fun makeIcon (color: Int, index: Int) : Bitmap{
        // create marker icon
        val icg = IconGenerator(this)
        icg.setColor(color)
        icg.setTextAppearance(Color.BLACK)
        val bm = icg.makeIcon((index).toString())

        return bm
    }

}


// __________________________________________________________________________________________________
// earlier tries with okhttp without retrofit

/*
        if (isOnline(this)) {
            progressMainBar.visibility = View.VISIBLE
            coroutine.launch {
                withContext(Dispatchers.IO) {

                    val client = OkHttpClient()

                    val request = Request.Builder()
                        .url("https://api.foursquare.com/v2/venues/search?query=coffee&ll=52.500342%2C13.425170&radius=5000&limit=5&client_id=1YTJGDPEFM0U5M3IPRIJAIGHISGHDT3IMKXIFJMYPWXB3ICQ&client_secret=VUIORTGVXQL2EGLKYJ10YRXJPDAIZSC3TXWFRQIYNGW10ZA3&v=20150101")
                        .get()
                        .addHeader("Accept", "application/json")
                        .build()

                    val response = client.newCall(request).execute()

                    val code = response.code
                    val body = response.body!!.string()
                    if (code == 200) response.body!!.close()

                    Log.d("response", body.toString())

                }
            }
            progressMainBar.visibility = View.INVISIBLE
        } else {
            Log.d("response", "offline".toString())
        }

    // check if use has internet connection
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


         */