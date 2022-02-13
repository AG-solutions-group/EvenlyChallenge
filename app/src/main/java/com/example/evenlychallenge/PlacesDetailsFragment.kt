package com.example.evenlychallenge

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.evenlychallenge.databinding.FragmentPlacesDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_places_details.*
import retrofit2.HttpException
import java.io.IOException


private lateinit var binding : FragmentPlacesDetailsBinding
private lateinit var place: Venue

class PlacesDetailsFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlacesDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load venue data from API
        loadDetails ()
        // initiate listeners using API data
        clickListeners ()
    }

    fun loadDetails() {
        // load the details if we get an ID from the click
        if (MapsActivity.clickedVenueID != "") {
            lifecycleScope.launchWhenCreated {
                (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = true
                // get the data via Foursquare && OkHttp && Retrofit Addon
                val response = try {
                    Utils.retrofitInstanceDetails(Utils.provideHttpClient()).getPlacesDetails(MapsActivity.clickedVenueID)
                } catch (e: IOException) {
                    Log.e("MainActivity", "IOException, you might not have internet connection")
                    (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = false
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    Log.e("MainActivity", "HttpException, unexpected response")
                    (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = false
                    return@launchWhenCreated
                }
                if (response.isSuccessful && response.body() != null) {
                    // TODO binding also viable but not really necessary in this use case
                        // save data in variable && set views
                    place = response.body()!!.response.venue
                    placeNameDetailsTV.text = place.name
                    if (place.location.address != "") placeAddressDetailsTV.text = place.location.address
                    if (place.rating != null) placeRatingDetailsTV.text = "Rating: " + place.rating.toString()
                    if (place.bestPhoto == null || place.bestPhoto.prefix == "" || place.bestPhoto.prefix == null) {
                        placeLogoDetailsIV.setImageDrawable(getDrawable(requireActivity() as MapsActivity, R.drawable.evenlylogo))
                    } else {
                        // create a picture from URL via Picasso Addon
                        Picasso.get()
                            .load(place.bestPhoto.prefix + "88" + place.bestPhoto.suffix)
                            .resize(80, 80)
                            .centerCrop()
                            .into(placeLogoDetailsIV)
                    }
                } else {
                    Log.e("MainActivity", "Response not successful")
                }
                (requireActivity() as MapsActivity).binding.progressMainBar.isVisible = false
            }
        }
    }

    fun clickListeners (){
        // share the canonical link && add venue name and message
        shareBtn.setOnClickListener(){
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, place.name)
                var shareMessage = "Check out this cool place!\n\n"
                shareMessage += place.canonicalUrl
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Where do you want to choose?"))
            } catch (e: Exception) {
                Log.e("Nope. Not sharing", e.toString())
            }
        }
        // open google maps app at the location && use the name
        openMapsBtn.setOnClickListener(){
            val gmmIntentUri = Uri.parse("geo:${place.location.lat},${place.location.lng}?q=" + Uri.encode(place.name))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}