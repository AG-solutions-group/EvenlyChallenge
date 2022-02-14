package com.example.evenlychallenge


import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evenlychallenge.MapsActivity.Companion.mMap
import com.example.evenlychallenge.MapsActivity.Companion.markerList
import com.example.evenlychallenge.databinding.FragmentPlacesOverviewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.fragment_places_overview.view.*
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.floor


private lateinit var binding : FragmentPlacesOverviewBinding
private lateinit var placesAdapter: PlacesAdapter

class PlacesOverviewFragment : Fragment(), PlacesAdapter.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlacesOverviewBinding.inflate(inflater)
     //   binding = inflater.inflate(R.layout.fragment_places_overview, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load recycler
        setupRecyclerView()
        // load API data
        loadData ()


    }

    private fun setupRecyclerView() = binding.placesOverviewRecycler.apply {

        // set up recycler - only adjust for span count here so that at least all icons are properly visible

        val width = resources.displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        val dpWidth = width / density
        val spanCount = (floor(dpWidth / 100)).toInt()

        placesAdapter = PlacesAdapter(this@PlacesOverviewFragment)
        adapter = placesAdapter
        layoutManager = GridLayoutManager(activity, spanCount)
        val spacesItemDecoration = SpacesItemDecoration(spanCount, resources.getDimensionPixelSize(R.dimen.recycler_view_item_width))
        this.addItemDecoration(spacesItemDecoration)



        // TODO follow material design guidelines to make grid design pretty
    }

    fun loadData(){
        lifecycleScope.launchWhenCreated {
            (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = true
            val response = try {
                // get the data via Foursquare && OkHttp && Retrofit Addon
                Utils.retrofitInstance(Utils.provideHttpClient()).getPlaces()
            } catch(e: IOException) {
                Log.e("MainActivity", "IOException, you might not have internet connection")
                (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = false
                val evenlyHQ = LatLng(52.500342, 13.425170)
                mMap.addMarker(MarkerOptions().position(evenlyHQ).title("Evenly HQ"))!!.setIcon(BitmapDescriptorFactory.fromResource(
                    com.example.evenlychallenge.R.drawable.evenlypin))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(evenlyHQ, 14f))
                Toast.makeText((requireActivity() as MapsActivity), "IOException, you might not have internet connection", Toast.LENGTH_LONG).show()
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e("MainActivity", "HttpException, unexpected response")
                (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = false
                val evenlyHQ = LatLng(52.500342, 13.425170)
                mMap.addMarker(MarkerOptions().position(evenlyHQ).title("Evenly HQ"))!!.setIcon(BitmapDescriptorFactory.fromResource(
                    com.example.evenlychallenge.R.drawable.evenlypin))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(evenlyHQ, 14f))
                Toast.makeText((requireActivity() as MapsActivity), "HttpException, unexpected response", Toast.LENGTH_LONG).show()
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null) {
                // put the data into the recycler view
                var index = 1
                placesAdapter.places = response.body()!!.response.venues
                placesAdapter.places.forEach {
                    // make custom marker icon
                    val icg = IconGenerator(requireActivity() as MapsActivity)
                    icg.setColor(Color.parseColor("#FDC26C")) //
                    icg.setTextAppearance(Color.BLACK) //
                    val bm = icg.makeIcon(index.toString())

                    // add marker to google map
                    var marker = mMap.addMarker(MarkerOptions()
                        .position(LatLng(it.location.lat, it.location.lng))
                        .title(it.name.toString())
                        .icon(BitmapDescriptorFactory.fromBitmap(bm))
                    )
                    // get the venue id to later load venue details
                    marker!!.tag = it.id
                    // put all markers in a list to be able to retrieve specific markers later
                    markerList.add(markerList.size, marker)
                    index++
                }

                // Add a marker at Evenly HQ and move the camera
                val evenlyHQ = LatLng(52.500342, 13.425170)
                mMap.addMarker(MarkerOptions().position(evenlyHQ).title("Evenly HQ"))!!.setIcon(BitmapDescriptorFactory.fromResource(
                    com.example.evenlychallenge.R.drawable.evenlypin))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(evenlyHQ, 14f))
            } else {
                Log.e("MainActivity", "Response not successful")
            }
            (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = false
        }
    }

    override fun onClick(position: Int) {
        // change marker color from click in recycler
        (requireActivity() as MapsActivity).changeMarker(markerList[position])
    }
}

class SpacesItemDecoration(private val spanCount: Int, private val viewWidth: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val width = parent.width
        val childWidth = viewWidth * spanCount
        val space = (width - childWidth) / spanCount
        var top = 0

        if (((parent.getChildLayoutPosition(view) + 3) / spanCount) <= 1) {
            top = (space / 2).toInt()
        } else {
            top = (space / 8).toInt()
        }

        outRect.set(
            (space / 2).toInt(),
            top,
            (space / 2).toInt(),
            (space / 2).toInt())

    }
}